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
package org.exoplatform.resolver;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 * @version $Id$
 *
 */
public class ClasspathResourceResolver extends ResourceResolver {

	
	
	@Override
	public URL getResource(String url) throws Exception {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		return cl.getResource(removeScheme(url));
	}

	@Override
	public InputStream getInputStream(String url) throws Exception {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		return cl.getResourceAsStream(removeScheme(url));
	}

	@Override
	public List<URL> getResources(String url) throws Exception {
		List<URL> urlList = new ArrayList<URL>();
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		Enumeration<URL> e = cl.getResources(removeScheme(url));
		while(e.hasMoreElements()) {
			urlList.add(e.nextElement());
		}
		return urlList;
	}

	@Override
	public List<InputStream> getInputStreams(String url) throws Exception {
		List<InputStream> urlList = new ArrayList<InputStream>();
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		Enumeration<URL> e = cl.getResources(removeScheme(url));
		while(e.hasMoreElements()) {
			urlList.add(e.nextElement().openStream());
		}
		return urlList;
	}

	@Override
	public String getResourceScheme() {
		return "classpath:";
	}

	@Override
	boolean isModified(String url, long lastAccess) {
		return false;
	}
}
