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

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.naming.InitialContextInitializer;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 * @version $Id$
 *
 */
@ConfiguredBy({@ConfigurationUnit(scope = ContainerScope.PORTAL, path = "conf/test-configuration.xml")})
public class BootstrapTestCase extends AbstractKernelTest {

	private String previousTmpDirPath = null;
	
	public void testRequestLifeCycle() {
		PortalContainer container = PortalContainer.getInstance();
		CustomService testService = (CustomService)container.getComponentInstanceOfType(CustomService.class);
		assertNotNull(testService);
		assertNull(testService.currentContainer);
		begin();
		assertSame(testService.currentContainer, container);
		end();
		assertNull(testService.currentContainer);
	}
	
	public void testDataSource() throws Exception {
		PortalContainer container = PortalContainer.getInstance();
		container.getComponentInstanceOfType(InitialContextInitializer.class);
		DataSource ds = (DataSource)new InitialContext().lookup("jdbcexo");
		assertNotNull(ds);
	}
}
