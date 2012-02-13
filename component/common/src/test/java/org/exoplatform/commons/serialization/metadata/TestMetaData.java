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
package org.exoplatform.commons.serialization.metadata;

import java.util.ArrayList;
import java.util.LinkedList;

import org.exoplatform.commons.serialization.model.ClassTypeModel;
import org.exoplatform.commons.serialization.model.ConvertedTypeModel;
import org.exoplatform.commons.serialization.model.SerializationMode;
import org.exoplatform.commons.serialization.model.TypeDomain;
import org.exoplatform.commons.serialization.model.metadata.DomainMetaData;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 * @version $Id$
 *
 */
public class TestMetaData extends TestCase {

	public void testSerializedObjectClassType() throws Exception {
		DomainMetaData domainMD = new DomainMetaData();
		domainMD.addClassType(Object.class, true);
		TypeDomain typeDomain = new TypeDomain(domainMD);
		ClassTypeModel<Object> objectTM = (ClassTypeModel<Object>)typeDomain.addTypeModel(Object.class);
		assertEquals(SerializationMode.SERIALIZED, objectTM.getSerializationMode());
	}
	
	public void testObjectClassType() throws Exception {
		DomainMetaData domainMD = new DomainMetaData();
		domainMD.addClassType(Object.class, false);
		TypeDomain typeDomain = new TypeDomain(domainMD);
		ClassTypeModel<Object> objectTM = (ClassTypeModel<Object>)typeDomain.addTypeModel(Object.class);
		assertEquals(SerializationMode.NONE, objectTM.getSerializationMode());
	}
	
	public void testSerializedStringClassType() throws Exception {
		DomainMetaData domainMD = new DomainMetaData();
		domainMD.addClassType(String.class, true) ;
		TypeDomain typeDomain = new TypeDomain(domainMD);
		ClassTypeModel<String> stringTM = (ClassTypeModel<String>)typeDomain.addTypeModel(String.class);
		assertEquals(SerializationMode.SERIALIZED, stringTM.getSerializationMode());
	}
	
	public void testStringClassType() throws Exception {
		DomainMetaData domainMD = new DomainMetaData();
		TypeDomain typeDomain = new TypeDomain(domainMD);
		ClassTypeModel<String> stringTM = (ClassTypeModel<String>)typeDomain.addTypeModel(String.class);
		assertEquals(SerializationMode.SERIALIZABLE, stringTM.getSerializationMode());
	}
	
	public void testThreadConvertedType() throws Exception {
		DomainMetaData domainMD = new DomainMetaData();
		domainMD.addConvertedType(Thread.class, ThreadTypeConverter.class);
		TypeDomain typeDomain = new TypeDomain(domainMD);
		ConvertedTypeModel<Thread, String> threadTM = (ConvertedTypeModel<Thread, String>)typeDomain.addTypeModel(Thread.class);
		assertEquals(ThreadTypeConverter.class, threadTM.getConverterJavaType());
	}
	
	public void testArrayListConvertedType() throws Exception {
		DomainMetaData domainMD = new DomainMetaData();
		domainMD.addConvertedType(ArrayList.class, ArrayListTypeConverter.class);
		TypeDomain typeDomain = new TypeDomain(domainMD);
		ConvertedTypeModel<ArrayList, LinkedList> objectTM = (ConvertedTypeModel<ArrayList, LinkedList>)typeDomain.addTypeModel(ArrayList.class);
		assertEquals(ArrayListTypeConverter.class, objectTM.getConverterJavaType());
	}
}
