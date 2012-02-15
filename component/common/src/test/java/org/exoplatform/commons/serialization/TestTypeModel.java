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
package org.exoplatform.commons.serialization;

import java.util.Map;

import org.exoplatform.commons.serialization.model.ClassTypeModel;
import org.exoplatform.commons.serialization.model.FieldModel;
import org.exoplatform.commons.serialization.model.TypeDomain;
import org.exoplatform.commons.serialization.model.TypeModel;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 * @version $Id$
 *
 */
public class TestTypeModel extends TestCase {
	
	public void testBar() {
		TypeDomain domain = new TypeDomain();
		assertEquals(0, domain.getSize());
	}
	
	public void testFoo() {
		TypeDomain domain = new TypeDomain();
		assertType(String.class, domain.addTypeModel(String.class));
		assertEquals(5, domain.getSize());
		
		assertType(Object.class, domain.getTypeModel(Object.class));
		assertType(char[].class, domain.getTypeModel(char[].class));
		assertType(Integer.class, domain.getTypeModel(Integer.class));
		assertType(Number.class, domain.getTypeModel(Number.class));
	}
	
	public void testJuu() {
		TypeDomain domain = new TypeDomain();
		ClassTypeModel<A> aTM = (ClassTypeModel<A>)domain.addTypeModel(A.class);
		assertEquals(A.class.getName(), aTM.getName());
		assertSame(A.class, aTM.getJavaType());
		
		Map<String, FieldModel<A, ?>> fieldMap = aTM.getFieldMap();
		assertEquals(3, fieldMap.size());
		
		FieldModel aFM = fieldMap.get("a");
		assertEquals("a", aFM.getName());
		assertEquals(domain.getTypeModel(String.class), aFM.getType());
		
		FieldModel bFM = fieldMap.get("b");
		assertEquals("b", bFM.getName());
		assertEquals(domain.getTypeModel(Integer.class), bFM.getType());
		
		FieldModel cFM = fieldMap.get("c");
		assertEquals("c", cFM.getName());
		assertEquals(domain.getTypeModel(Boolean.class), cFM.getType());
	}
	
	public void testDoubleAdd() {
		TypeDomain domain = new TypeDomain();
		ClassTypeModel<A> aTM1 = (ClassTypeModel<A>)domain.addTypeModel(A.class);
		ClassTypeModel<A> aTM2 = (ClassTypeModel<A>)domain.addTypeModel(A.class);
		assertSame(aTM1, aTM2);
	}

	public void assertType(Class<?> clazz, TypeModel model) {
		assertTrue(model instanceof ClassTypeModel);
		ClassTypeModel serializableTypeModel = (ClassTypeModel)model;
		assertEquals(clazz, serializableTypeModel.getJavaType());
	}
}
