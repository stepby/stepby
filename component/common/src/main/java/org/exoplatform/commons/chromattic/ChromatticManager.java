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
package org.exoplatform.commons.chromattic;

import java.util.HashMap;
import java.util.Map;

import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.component.ComponentPlugin;
import org.exoplatform.container.component.ComponentRequestLifecycle;
import org.exoplatform.services.jcr.RepositoryService;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 * @version $Id$
 *
 */
public class ChromatticManager implements ComponentRequestLifecycle {
	
	final RepositoryService repositoryService;
	
	Map<String, String> lifeCycleToWorkspaceMap;
	
	Map<String, ChromatticLifeCycle> lifeCycles = new HashMap<String, ChromatticLifeCycle>();
	
	private final ThreadLocal<Synchronization> currentSynchronization = new ThreadLocal<Synchronization>();
	
	public ChromatticManager(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
		lifeCycleToWorkspaceMap = new HashMap<String, String>();
	}
	
	public ChromatticLifeCycle getLifeCycle(String lifeCycleName) {
		return lifeCycles.get(lifeCycleName);
	}
	
	//Called by kernel
	public void addLifeCycle(ComponentPlugin plugin) {
		ChromatticLifeCycle lifeCycle = (ChromatticLifeCycle)plugin;
		try {
			lifeCycle.manager = this;
			lifeCycle.start();
			lifeCycles.put(lifeCycle.getDomainName(), lifeCycle);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public Synchronization getSynchronization() {
		return currentSynchronization.get();
	}
	
	/**
	 * Begins the demarcation of a request and associates the current thread of execution with
	 * a context that will provides access to the correct persistence context
	 *  
	 * @throws IllegalStateException if request is already associated with this thread
	 */
	public void beginRequest() throws IllegalStateException {
		if(currentSynchronization.get() != null) 
			throw new IllegalStateException("Request already started");
		
		Synchronization sync = new Synchronization();
		currentSynchronization.set(sync);
	}
	
	/**
	 * End the demarcation of request
	 * 
	 * @param save to save the state
	 * @throws IllegalStateException if no request was started previously
	 * */
	public void endRequest(boolean save) throws IllegalStateException {
		Synchronization sync = currentSynchronization.get();
		
		//TODO
		currentSynchronization.set(null);
		
		if(sync == null) 
			throw new IllegalStateException("Request not started");
		
		sync.close(save);
		
		currentSynchronization.set(null);
	}

	@Override
	public void startRequest(ExoContainer container) {
		beginRequest();
	}

	@Override
	public void endRequest(ExoContainer container) {
		Synchronization sync = currentSynchronization.get();
		boolean save = sync.getSaveOnClose();
		endRequest(save);
	}
}
