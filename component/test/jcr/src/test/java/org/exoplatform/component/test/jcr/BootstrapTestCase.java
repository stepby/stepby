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
package org.exoplatform.component.test.jcr;

import javax.jcr.Session;

import org.exoplatform.component.test.AbstractKernelTest;
import org.exoplatform.component.test.ConfigurationUnit;
import org.exoplatform.component.test.ConfiguredBy;
import org.exoplatform.component.test.ContainerScope;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.jcr.RepositoryService;
import org.exoplatform.services.jcr.core.ManageableRepository;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 * @version $Id$
 *
 */
@ConfiguredBy({@ConfigurationUnit(scope = ContainerScope.PORTAL, path = "conf/exo.portal.component.test.jcr-configuration.xml")})
public class BootstrapTestCase extends AbstractKernelTest {
	
	public void testWorkspace() throws Exception {
		PortalContainer container = PortalContainer.getInstance();
		RepositoryService repos = (RepositoryService)container.getComponentInstance(RepositoryService.class);
		assertNotNull(repos);
		ManageableRepository repo = repos.getDefaultRepository();
		assertNotNull(repo);
		Session session = repo.getSystemSession("portal-test");
		assertNotNull(session);
		session.logout();
	}
}
