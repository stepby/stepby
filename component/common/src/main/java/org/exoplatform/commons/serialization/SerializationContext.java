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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

import org.exoplatform.commons.serialization.api.factory.DefaultObjectFactory;
import org.exoplatform.commons.serialization.api.factory.ObjectFactory;
import org.exoplatform.commons.serialization.model.TypeDomain;
import org.exoplatform.commons.serialization.serial.ObjectReader;
import org.exoplatform.commons.serialization.serial.ObjectWriter;

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
	
	public <O> O clone(O o) throws IOException, ClassNotFoundException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectWriter writer = new ObjectWriter(this, baos);
		writer.writeObject(o);
		writer.close();
		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		ObjectReader reader = new ObjectReader(this, bais);
		return (O)reader.readObject();
	}
	
	public void write(Object o, OutputStream out) throws IOException {
		ObjectWriter writer = new ObjectWriter(this, out);
		writer.writeObject(o);
		writer.flush();
	}
	
	public byte[] write(Object o) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectWriter writer = new ObjectWriter(this, out);
		writer.writeObject(o);
		writer.close();
		return out.toByteArray();
	}
	
	public Object read(byte[] bytes) throws IOException, ClassNotFoundException {
		ByteArrayInputStream in = new ByteArrayInputStream(bytes);
		ObjectReader reader = new ObjectReader(this, in);
		return reader.readObject();
	}
	
	public Object read(InputStream in) throws IOException, ClassNotFoundException {
		ObjectReader reader = new ObjectReader(this, in);
		return reader.readObject();
	}
}

