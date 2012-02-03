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

import org.exoplatform.commons.utils.ListAccess;
import org.exoplatform.commons.utils.PageList;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 * @version $Id$
 * 
 */
public class EmptySerializablePageList<E> extends AbstractSerializablePageList<E> implements Serializable {

	private static final EmptySerializablePageList instance = new EmptySerializablePageList();

	private transient final ListAccess<E> listAccess = new ListAccess<E>() {

		@Override
		public E[] load(int index, int length) throws Exception, IllegalArgumentException {
			throw new UnsupportedOperationException("should not be called");
		}

		@Override
		public int getSize() throws Exception {
			return 0;
		}
	};

	public EmptySerializablePageList() {
		super(10);
	}

	@Override
	protected ListAccess<E> connect() throws Exception {
		return listAccess;
	}

	public static <E> PageList<E> get() {
		// Cast OK
		return (PageList<E>)instance;
	}

	private Object readResolve() {
		return instance;
	}
}
