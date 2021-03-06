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
package org.exoplatform.commons.serialization.model;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.exoplatform.commons.serialization.model.metadata.DomainMetaData;
import org.gatein.common.logging.Logger;
import org.gatein.common.logging.LoggerFactory;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 * @version $Id$
 *
 */
public final class TypeDomain {

	private static final Logger log = LoggerFactory.getLogger(TypeDomain.class);
	
	private final DomainMetaData metaData;
	
	private final Map<String, TypeModel<?>> typeModelMap;
	
	private final Map<String, TypeModel<?>> immutableTypeModelMap;
	
	private final Collection<TypeModel<?>> immutableTypeModelSet;
	
	private final boolean buildIfAbsent;
	
	private final Object lock;
	
	public TypeDomain(boolean buildIfAbsent) {
		this(new DomainMetaData(), buildIfAbsent);
	}
	
	public TypeDomain() {
		this(new DomainMetaData(), false);
	}
	
	public TypeDomain(DomainMetaData metaData) {
		this(metaData, false);
	}
	
	public TypeDomain(DomainMetaData metaData, boolean buildIfAbsent) {
		ConcurrentHashMap<String, TypeModel<?>> typeModelMap = new ConcurrentHashMap<String, TypeModel<?>>();
		Map<String, TypeModel<?>> immutableTypeModelMap = Collections.unmodifiableMap(typeModelMap);
		Collection<TypeModel<?>> immutableTypeModelSet = Collections.unmodifiableCollection(typeModelMap.values());
		
		this.typeModelMap = typeModelMap;
		this.immutableTypeModelMap = immutableTypeModelMap;
		this.immutableTypeModelSet = immutableTypeModelSet;
		this.metaData = new DomainMetaData(metaData);
		this.buildIfAbsent = buildIfAbsent;
		this.lock = new Object();
	}
	
	public Map<String, TypeModel<?>> getTypeModelMap() {
		return immutableTypeModelMap;
	}
	
	public boolean getBuildIfAbsent() {
		return buildIfAbsent;
	}
	
	public Collection<TypeModel<?>> getTypeModels() {
		return immutableTypeModelSet;
	}
	
	public TypeModel<?> getTypeModel(String name) {
		if(name == null) throw new NullPointerException();
		return typeModelMap.get(name);
	}
	
	public <O> TypeModel<O> getTypeModel(Class<O> javaType) {
		return get(javaType, buildIfAbsent);
	}
	
	public <O> TypeModel<O> addTypeModel(Class<O> javaType) {
		return get(javaType, true);
	}
	
	private <O> TypeModel<O> get(Class<O> javaType, boolean create) {
		if(javaType == null) throw new NullPointerException();
		
		TypeModel<O> model = (TypeModel<O>)typeModelMap.get(javaType.getName());
		
		if(model == null && create) {
			synchronized (lock) {
				TypeModelBuilder builder = new TypeModelBuilder(metaData, immutableTypeModelMap);
				model = builder.build(javaType);
				typeModelMap.putAll(builder.getAddedTypeModels());
			}
		}
		return model;
	}
	
	public int getSize() {
		return typeModelMap.size();
	}
}
