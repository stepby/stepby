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
package org.exoplatform.commons.serialization.converter;

import java.io.InvalidObjectException;

import org.exoplatform.commons.serialization.SerializationContext;
import org.exoplatform.commons.serialization.api.TypeConverter;
import org.exoplatform.commons.serialization.model.TypeDomain;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 * @version $Id$
 *
 */
public class TestConverter extends TestCase {

	public void testConvertSerializedType() throws Exception {
		TypeDomain domain = new TypeDomain();
		domain.addTypeModel(A_External.class);
		A_External e = new A_External("foo");
		
		A_Converter.delegate = new TypeConverter<A_External, A_Internal>() {
			@Override
			public A_Internal write(A_External input) throws Exception {
				return new A_Internal(input.state + "_converted");
			}
			@Override
			public A_External read(A_Internal output) throws Exception {
				return new A_External(output.state);
			}
		};
		
		SerializationContext context = new SerializationContext(domain);
		A_External clone = context.clone(e);
		assertEquals("foo_converted", clone.state);
	}
	
	public void testConvertSerializableType() throws Exception {
		TypeDomain domain = new TypeDomain();
		domain.addTypeModel(B_External.class);
		B_External e = new B_External("foo");

		SerializationContext context = new SerializationContext(domain);
		assertEquals("foo", context.clone(e).state);
	}
	
	public void testConvertWriteThrowException() throws Exception {
		TypeDomain domain = new TypeDomain();
		domain.addTypeModel(A_External.class);
		A_External a = new A_External("foo");
		
		final Exception e = new Exception();
		A_Converter.delegate = new TypeConverter<A_External, A_Internal>() {
			@Override
			public A_Internal write(A_External inputClass) throws Exception {
				throw e;
			}

			@Override
			public A_External read(A_Internal outputClass) throws Exception {
				throw new AssertionFailedError();
			}
		};
		
		SerializationContext context = new SerializationContext(domain);
		try {
			context.clone(a);
			fail();
		} catch (InvalidObjectException ioe) {
			assertSame(e, ioe.getCause());
		}
	}
	
	public void testConvertWriteReturnsNull() throws Exception {
		TypeDomain domain = new TypeDomain();
		domain.addTypeModel(A_External.class);
		A_External a = new A_External();
		A_Converter.delegate = new TypeConverter<A_External, A_Internal>() {
			@Override
			public A_Internal write(A_External inputClass) throws Exception {
				return null;
			}
			@Override
			public A_External read(A_Internal outputClass) throws Exception {
				throw new AssertionFailedError();
			}
		};
		
		SerializationContext context = new SerializationContext(domain);
		try {
			context.clone(a);
			fail();
		} catch(InvalidObjectException e) {}
	}
}
