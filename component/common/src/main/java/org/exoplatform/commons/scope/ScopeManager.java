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
package org.exoplatform.commons.scope;

import javax.jcr.RepositoryException;

import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.component.ComponentRequestLifecycle;
import org.exoplatform.services.jcr.RepositoryService;
import org.exoplatform.services.jcr.core.ManageableRepository;
import org.gatein.common.logging.Logger;
import org.gatein.common.logging.LoggerFactory;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 * @version $Id$
 *
 */
public class ScopeManager implements ComponentRequestLifecycle {
	
	private static final Logger log = LoggerFactory.getLogger(ScopeManager.class);
	
	private static final ThreadLocal<String> currentScope = new ThreadLocal<String>();
	
	public static String getCurrentScope() {
		return currentScope.get();
	}

	@Override
	public void startRequest(ExoContainer container) {
		if(currentScope.get() != null) {
			throw new IllegalStateException("Detected scope reentrancy " + currentScope.get());
		}
		
		RepositoryService repoService = (RepositoryService)container.getComponentInstanceOfType(RepositoryService.class);
		String scope = null;
		if(repoService != null) {
			try {
				ManageableRepository currentRepo = repoService.getCurrentRepository();
				scope = currentRepo.getConfiguration().getName();
			} catch(RepositoryException e) {
				log.error("Could not obtain scope value from repository", e);
			}
		}
		
		if(scope == null) {
			scope = "";
		}
		
		currentScope.set(scope);
		log.debug("Start scope request\"" + scope + "\"");
	}

	@Override
	public void endRequest(ExoContainer container) {
		if(currentScope.get() == null) {
			throw new IllegalStateException("Detected unscoped unscoping");
		}
		
		currentScope.set(null);
	}
}
