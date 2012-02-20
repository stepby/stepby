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

import java.io.Serializable;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 * @version $Id$
 *
 */
public class ScopedKey<S extends Serializable> extends AbstractScopedKey {

	private final S key;
	
	public static <S extends Serializable> ScopedKey<S> create(S key) throws NullPointerException {
		return new ScopedKey<S>(key);
	}
	
	public static <S extends Serializable> ScopedKey<S> create(String scope, S key) throws NullPointerException {
		return new ScopedKey<S>(scope, key);
	}
	
	public ScopedKey(S key) throws NullPointerException {
		if(key == null) throw new NullPointerException();
		this.key = key;
	}
	
	public ScopedKey(String scope, S key) throws NullPointerException {
		super(scope);
		if(key == null) throw new NullPointerException();
		this.key = key;
	}
	
	public S getKey() {
		return key;
	}
	
	@Override
	public int hashCode() {
		return super.hashCode() ^ key.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		return super.equals(obj) && ((ScopedKey)obj).key.equals(key);
	}
	
	@Override
	public String toString() {
		return "ScopedKey[scope=" + getScope() + ", key=" + key + "]";
	}
}
