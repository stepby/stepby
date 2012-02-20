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

import org.exoplatform.component.test.AbstractKernelTest;
import org.exoplatform.component.test.ConfigurationUnit;
import org.exoplatform.component.test.ConfiguredBy;
import org.exoplatform.component.test.ContainerScope;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 * @version $Id$
 *
 */

@ConfiguredBy({
	@ConfigurationUnit(scope = ContainerScope.PORTAL, path = "conf/exo.portal.component.test.jcr-configuration.xml"),
	@ConfigurationUnit(scope = ContainerScope.PORTAL, path = "org/exoplatform/commons/scope/configuration.xml")
})
public class ScopeTestCase extends AbstractKernelTest {
	
	public void testKey() {
		ScopedKey<String> key1  = ScopedKey.create("foo", "bar");
		assertEquals("foo", key1.getScope());
		assertEquals("bar", key1.getKey());
		
		ScopedKey<String> key2 = ScopedKey.create("foo", "bar");
		assertEquals("foo",  key2.getScope());
		assertEquals("bar", key2.getKey());
		
		assertTrue(key1.equals(key2));
		assertTrue(key2.equals(key1));
		assertEquals(key1.hashCode(), key2.hashCode());
		
		ScopedKey<String> key3 = ScopedKey.create("juu", "bar");
		ScopedKey<String> key4 = ScopedKey.create("juu", "daa");
		assertFalse(key1.equals(key3));
		assertFalse(key3.equals(key1));
		assertFalse(key3.equals(key4));
		assertFalse(key4.equals(key3));
	}
	
	public void testLifeCycle() {
		assertNull(ScopeManager.getCurrentScope());
		ScopedKey<String> key = ScopedKey.create("foo");
		assertEquals("", key.getScope());
		assertEquals("foo", key.getKey());
		
		begin();
		assertEquals("repository", ScopeManager.getCurrentScope());
		key = ScopedKey.create("foo");
		assertEquals("foo", key.getKey());
		assertEquals("repository", key.getScope());
		
		end();
		assertNull(ScopeManager.getCurrentScope());
		key = ScopedKey.create("foo");
		assertEquals("foo", key.getKey());
		assertEquals("", key.getScope());
	}
}
