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

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Arrays;

import org.gatein.common.io.IOTools;
import org.gatein.common.io.UndeclaredIOException;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 * @version $Id$
 *
 */
public class MarshalledObject<S extends Serializable> {

	private final ClassLoader loader;
	private final byte[] state;
	
	public MarshalledObject(ClassLoader loader, byte[] state) {
		this.loader = loader;
		this.state = state;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == this) return true;
		else if(obj instanceof MarshalledObject) {
			MarshalledObject<?> that = (MarshalledObject<?>)obj;
			return Arrays.equals(that.state, state);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode(state);
	}
	
	public static <S extends Serializable>  MarshalledObject<S> marshall(S serializable) {
		if(serializable == null) throw new NullPointerException();
		try {
			byte[] bytes = IOTools.serialize(serializable);
			return new MarshalledObject<S>(serializable.getClass().getClassLoader(), bytes);
		} catch(IOException e) {
			throw new UndeclaredIOException(e);
		}
	}
	
	public S unmarshall() throws UndeclaredThrowableException {
		try {
			return (S)IOTools.unserialize(state, loader);
		} catch(IOException e) {
			throw new UndeclaredIOException(e);
		} catch(ClassNotFoundException e) {
			throw new UndeclaredThrowableException(e);
		}
	}
}
