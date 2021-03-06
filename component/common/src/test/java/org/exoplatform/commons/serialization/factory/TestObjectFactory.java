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
package org.exoplatform.commons.serialization.factory;

import java.io.InvalidClassException;

import org.exoplatform.commons.serialization.SerializationContext;
import org.exoplatform.commons.serialization.model.TypeDomain;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 * @version $Id$
 *
 */
public class TestObjectFactory extends TestCase {

	public void testCustomFactory() throws Exception {
		TypeDomain typeDomain = new TypeDomain();
		typeDomain.addTypeModel(A.class);
		SerializationContext context = new SerializationContext(typeDomain);
		context.addFactory(new CustomFactory());
		
		A a = new A();
		A clone = context.clone(a);
		assertSame(CustomFactory.instance, clone);
	}
	
	public void testFactoryThrowException() throws Exception {
		TypeDomain typeDomain = new TypeDomain();
		typeDomain.addTypeModel(B.class);
		SerializationContext context = new SerializationContext(typeDomain);
		B b = new B(false);
		try {
			context.clone(b);
			fail();
		} catch (InvalidClassException e) {
		}
	}
}
