/*
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
package org.exoplatform.commons.cache.future;

import java.util.concurrent.Callable;

import junit.framework.TestCase;

import org.exoplatform.commons.cache.future.FutureMap;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 * @version $Id$
 *
 * Mar 2, 2012
 */
public class GetTestCase extends TestCase {

	public void testGet() {
		FutureMap<String, String, Callable<String>> futureCache = new FutureMap<String, String, Callable<String>>(new StringLoader());
		assertEquals("foo_value", futureCache.get(new Callable<String>() {
			@Override
			public String call() throws Exception {
				return "foo_value";
			}
		}, "foo"));
		assertEquals("foo_value",futureCache.data.get("foo"));
	}
	
	public void testNullValue() {
		FutureMap<String, String, Callable<String>> futureCache = new FutureMap<String, String, Callable<String>>(new StringLoader());
		assertNull(futureCache.get(new Callable<String>() {
			@Override
			public String call() throws Exception {
				return null;
			}
		}, "foo"));
		
		assertNull(futureCache.data.get("foo"));
	}
	
	public void testThrowException() {
		FutureMap<String, String, Callable<String>> futureCache = new FutureMap<String, String, Callable<String>>(new StringLoader());
		assertNull(futureCache.get(new Callable<String>() {
			@Override
			public String call() throws Exception {
				throw new Exception();
			}
		}, "foo"));
		
		assertFalse(futureCache.data.containsKey("foo"));
	}
	
	public void testReentrancy() {
		final FutureMap<String, String, Callable<String>> futureCache = new FutureMap<String, String, Callable<String>>(new StringLoader());
		String res = futureCache.get(new Callable<String>() {

			@Override
			public String call() throws Exception {
				try {
					futureCache.get(new Callable<String>() {
						@Override
						public String call() throws Exception {
							throw new AssertionError();
						}
					}, "foo");
					return "fail";
				} catch(IllegalStateException expected) {
					return "pass";
				}
			}
		}, "foo");
		
		assertEquals("pass", res);
	}
}
