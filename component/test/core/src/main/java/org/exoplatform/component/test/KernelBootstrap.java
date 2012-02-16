/*
 * Copyright (C) 2012 eXo Platform SAS.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.exoplatform.component.test;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import junit.framework.AssertionFailedError;

import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.RootContainer;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 * @version $Id$
 *
 */
public class KernelBootstrap {

	private static final String TMP_DIR = "gatein.test.tmp.dir";
	
	private File tmpDir;
	
	private File targetDir;
	
	private EnumMap<ContainerScope, Set<String>> configs;
	
	private ClassLoader realClassLoader;
	
	private PortalContainer container;
	
	public KernelBootstrap() {
		this(Thread.currentThread().getContextClassLoader());
	}
	
	public KernelBootstrap(ClassLoader realClassLoader) {
		Set<String> rootConfigPaths = new LinkedHashSet<String>();
		rootConfigPaths.add("conf/root-configuration.xml");
		Set<String> portalConfigPaths = new LinkedHashSet<String>();
		portalConfigPaths.add("conf/portal-configuration.xml");
		EnumMap<ContainerScope, Set<String>> configs = new EnumMap<ContainerScope, Set<String>>(ContainerScope.class);
		configs.put(ContainerScope.ROOT, rootConfigPaths);
		configs.put(ContainerScope.PORTAL, portalConfigPaths);
		
		File targetDir = new File(new File(System.getProperty("basedir")), "target");
		if(!targetDir.exists()) {
			throw new AssertionFailedError("Target dir for unit test does not exist");
		} 
		if(!targetDir.isDirectory()) {
			throw new AssertionFailedError("Target dir is not directory");
		}
		if(!targetDir.canWrite()) {
			throw new AssertionFailedError("Target dir is not writable");
		}
		
		this.configs = configs;
		this.targetDir = targetDir;
		this.tmpDir = findTmpDir(targetDir);
		this.realClassLoader = realClassLoader;
	}
	
	private static File findTmpDir(File dir) {
		Set<String> fileNames = new HashSet<String>();
		for(File child : dir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.startsWith("gateintest-");
			}
		}))
		{
			fileNames.add(child.getName());
		}
		
		String fileName;
		int count = 0;
		while(true) {
			fileName = "gateintest-" + count;
			if(!fileNames.contains(fileName)) break;
			count++;
		}
		
		return new File(dir, fileName);
	}
	
	public File getTargetDir() {
		return this.targetDir;
	}
	
	public File getTmpDir() {
		return this.tmpDir;
	}
	
	public void setTmpDir(File tmpDir) throws IllegalArgumentException {
		for(File parent = tmpDir.getParentFile();!targetDir.equals(parent); parent = parent.getParentFile()) {
			if(parent == null) throw new IllegalArgumentException("wrong tmp dir" + tmpDir);
		}
		this.tmpDir = tmpDir;
	}
	
	public PortalContainer getContainer() {
		return container;
	}

	public void addConfiguration(ContainerScope scope, String path) {
		configs.get(scope).add(path);
	}
	
	public void addConfiguration(ConfigurationUnit unit) {
		configs.get(unit.scope()).add(unit.path());
	}
	
	public void addConfiguration(ConfiguredBy configuredBy) {
		for(ConfigurationUnit unit : configuredBy.value()) {
			addConfiguration(unit);
		}
	}
	
	public void addConfiguration(Class<?> clazz) {
		ConfiguredBy configuredBy = clazz.getAnnotation(ConfiguredBy.class);
		if(configuredBy != null) addConfiguration(configuredBy);
	}
	
	public void boot() throws IllegalStateException {
		if(container != null) throw new IllegalStateException("Already booted");

		try {
			Field topContainerField = ExoContainerContext.class.getDeclaredField("topContainer");
			topContainerField.setAccessible(true);
			topContainerField.set(null, null);
			
			Field singletonField = RootContainer.class.getDeclaredField("singleton_");
			singletonField.setAccessible(true);
			singletonField.set(null, null);
			
			if(!tmpDir.exists()) {
				if(!tmpDir.mkdirs()) 
					throw new AssertionFailedError("Could not create	directory " + tmpDir.getAbsolutePath()); 
			}
			
			System.setProperty(TMP_DIR, tmpDir.getCanonicalPath());
			
			ClassLoader testClassLoader = new GateInTestClassLoader(
				realClassLoader, 
				configs.get(ContainerScope.ROOT), 
				configs.get(ContainerScope.PORTAL));
			
			Thread.currentThread().setContextClassLoader(testClassLoader);
			
			this.container = PortalContainer.getInstance();
		} catch (Exception e) {
			AssertionFailedError afe = new AssertionFailedError();
			afe.initCause(e);
			throw afe;
		} finally {
			Thread.currentThread().setContextClassLoader(realClassLoader);
		}
	}
	
	public void dispose() {
		if(container != null) {
			RootContainer.getInstance().stop();
			container = null;
			ExoContainerContext.setCurrentContainer(null);
		}
	}
}
