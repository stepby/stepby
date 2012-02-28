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

import javax.jcr.Session;
import javax.jcr.Workspace;

import org.chromattic.api.Chromattic;
import org.chromattic.api.ChromatticSession;
import org.exoplatform.component.test.AbstractKernelTest;
import org.exoplatform.component.test.ConfigurationUnit;
import org.exoplatform.component.test.ConfiguredBy;
import org.exoplatform.component.test.ContainerScope;
import org.exoplatform.container.PortalContainer;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 * @version $Id$
 *
 */

@ConfiguredBy({
	@ConfigurationUnit(scope = ContainerScope.PORTAL, path = "conf/exo.portal.component.test.jcr-configuration.xml"),
	@ConfigurationUnit(scope = ContainerScope.PORTAL, path = "org/exoplatform/commons/chromattic/configuration.xml")
})
public class ChromatticIntegrationTestCase extends AbstractKernelTest{

	private ChromatticLifeCycle test1LF;
	
	private ChromatticLifeCycle test2LF;
	
	private ChromatticManager chromatticManager;
	
	public ChromatticIntegrationTestCase() {
		
	}
	
	@Override
	protected void setUp() throws Exception {
		PortalContainer container = PortalContainer.getInstance();
		chromatticManager = (ChromatticManager)container.getComponent(ChromatticManager.class);
		test1LF = chromatticManager.getLifeCycle("test1");
		test2LF = chromatticManager.getLifeCycle("test2");
	}
	
	public void testConfiguratorInitialized() throws Exception {
		assertNotNull(test1LF);
		assertEquals("portal-test", test1LF.getWorkspaceName());
		assertNotNull(test2LF);
		assertEquals("portal-test", test2LF.getWorkspaceName());
		assertNotNull(test1LF.getChromattic());
		assertSame(chromatticManager, test1LF.getManager());
	}
	
	public void testCannotInitiateMoreThanOneRequest() {
		chromatticManager.beginRequest();
		
		try {
			chromatticManager.beginRequest();
			fail();
		} catch(IllegalStateException e) {
		}
		
		chromatticManager.endRequest(false);
	}
	
	public void testCannotEndNonExistingRequest() {
		try {
			chromatticManager.endRequest(false);
			fail();
		} catch(IllegalStateException e) {
		}
	}
	
	public void testWrapperFailsWhenNoGlobalRequest() throws Exception {
		try {
			test1LF.getChromattic().openSession();
			fail();
		} catch(IllegalStateException e) {
		}
	}
	
	public void testLocalRequest() throws Exception {
		Session jcrSession;
		
		SessionContext context = test1LF.openContext();
		try {
			ChromatticSession session = test1LF.getChromattic().openSession();
			FooEntity foo = session.create(FooEntity.class);
			assertEquals("portal-test", foo.getWorkspace());
			
			jcrSession = session.getJCRSession();
			assertTrue(jcrSession.isLive());
			
			Workspace workspace = jcrSession.getWorkspace();
			assertEquals("portal-test", workspace.getName());
			
			session.close();
			assertTrue(jcrSession.isLive());
		} finally {
			test1LF.closeContext(false);
		}
		
		assertFalse(jcrSession.isLive());
	}
	
	public void testGlobalContext() throws Exception {
		Session jcrSession;
		
		SynchronizationEventQueue queue = new SynchronizationEventQueue();
		
		chromatticManager.beginRequest();
		
		try {
			Chromattic chromattic = test1LF.getChromattic();
			assertNull(test1LF.getContext(true));
			
			ChromatticSession session = chromattic.openSession();
			
			SessionContext context = test1LF.getContext(true);
			assertNotNull(context);
			context.addSynchronizationListener(queue);
			
			FooEntity foo = session.create(FooEntity.class);
			assertEquals("portal-test", foo.getWorkspace());
			
			//Check related JCR session
			jcrSession = session.getJCRSession();
			assertTrue(jcrSession.isLive());
			Workspace workspace = jcrSession.getWorkspace();
			assertEquals("portal-test", workspace.getName());
			
			//Closing chromattic session should not close the underlying JCR session
			session.close();
			assertTrue(jcrSession.isLive());
			
			queue.assertEmpty();
		} finally {
			chromatticManager.endRequest(false);
		}
		
		queue.assertEvent(SynchronizationEvent.BEFORE);
		queue.assertEvent(SynchronizationEvent.DISCARDED);
		
		assertFalse(jcrSession.isLive());
	}
	
	public void testGlobalSessionContext() throws Exception {
		chromatticManager.beginRequest();
		try {
			SessionContext context = test1LF.getContext(true);
			assertNull(context);
			
			context = test1LF.getContext(false);
			assertNotNull(context);
		} finally {
			chromatticManager.endRequest(false);
		}
	}
	
	public void testPersistence() throws Exception {
		chromatticManager.beginRequest();
		ChromatticSession session = test1LF.getChromattic().openSession();
		FooEntity foo = session.create(FooEntity.class);
		String fooId = session.persist(foo, "testPersistence");
		session.save();
		chromatticManager.endRequest(true);
		
		chromatticManager.beginRequest();
		session = test1LF.getChromattic().openSession();
		foo = session.findById(FooEntity.class, fooId);
		session.close();
		chromatticManager.endRequest(false);
		
		assertNotNull(foo);
	}
	
	public void testTwoLifeCycleWithSameRepository() {
		chromatticManager.beginRequest();
		SessionContext ctx1 = test1LF.openContext();
		Session session1 = ctx1.getSession().getJCRSession();
		Session session2 = test2LF.getChromattic().openSession().getJCRSession();
		assertSame(session1, session2);
		chromatticManager.endRequest(false);
	}
}
