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

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

import org.exoplatform.commons.serialization.api.factory.DefaultObjectFactory;
import org.exoplatform.commons.serialization.api.factory.ObjectFactory;
import org.exoplatform.commons.serialization.model.TypeDomain;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 * @version $Id$
 *
 */
public class SerializationContext {

	private final TypeDomain typeDomain;
	
	private final Map<Class<?>, ObjectFactory<?>> factories;
	
	public SerializationContext(TypeDomain typeDomain) {
		HashMap<Class<?>, ObjectFactory<?>> factories = new HashMap<Class<?>, ObjectFactory<?>>();
		factories.put(Object.class, new DefaultObjectFactory());
		
		this.typeDomain = typeDomain;
		this.factories = factories;
	}
	
	public TypeDomain getTypeDomain() {
		return typeDomain;
	}
	
	public <O> void addFactory(ObjectFactory<O> factory) {
		Class<ObjectFactory<O>> factoryClass = (Class<ObjectFactory<O>>)factory.getClass();
		
		ParameterizedType pt = (ParameterizedType)factoryClass.getGenericSuperclass();
		
		Class<?> objectType = (Class<?>)pt.getActualTypeArguments()[0];
		
		factories.put(objectType, factory);
	}
	
	public <O> ObjectFactory<? super O> getFactory(Class<O> type) {
		ObjectFactory<O> factory = (ObjectFactory<O>)factories.get(type);
		
		if(factory == null) {
			return getFactory(type.getSuperclass());
		}
		
		return factory;
	}
}
