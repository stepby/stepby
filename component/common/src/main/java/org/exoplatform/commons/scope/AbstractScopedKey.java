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
public abstract class AbstractScopedKey implements Serializable {

	private final String scope;
	
	protected AbstractScopedKey() {
		String scope = ScopeManager.getCurrentScope();
		if(scope == null) scope = "";
		this.scope = scope;
	}
	
	protected AbstractScopedKey(String scope) throws NullPointerException {
		if(scope == null) throw new NullPointerException();
		this.scope = scope;
	}
	
	public final String getScope() {
		return scope;
	}
	
	@Override
	public int hashCode() {
		return scope.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == this) return true;
		if(obj instanceof AbstractScopedKey) {
			AbstractScopedKey that = (AbstractScopedKey)obj;
			return getClass().equals(that.getClass()) && scope.equals(that.scope);
		}
		return false;
	}
}
