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
package org.exoplatform.commons.util;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.List;

import org.exoplatform.commons.utils.ListAccess;
import org.gatein.common.util.ParameterValidation;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 * @version $Id$
 *
 */
public class ListAccessImpl<E> implements ListAccess<E>, Serializable{

	private static final long serialVersionUID = -2196777766886446803L;
	
	private final List<E> list;
	
	private final Class<E> elementType;
	
	public ListAccessImpl(Class<E> elementType, List<E> list) {
		ParameterValidation.throwIllegalArgExceptionIfNull(elementType, "element type");
		ParameterValidation.throwIllegalArgExceptionIfNull(list, "elements");
		this.elementType = elementType;
		this.list = list;
	}

	/**
	 * @see org.exoplatform.commons.utils.ListAccess#load(int, int)
	 */
	@Override
	public E[] load(int index, int length) throws Exception, IllegalArgumentException {
		E[] array = (E[])Array.newInstance(elementType, length);
		list.subList(index, index + length).toArray(array);
		return array;
	}

	/**
	 * @see org.exoplatform.commons.utils.ListAccess#getSize()
	 */
	@Override
	public int getSize() throws Exception {
		return list.size();
	}
}
