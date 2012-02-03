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

import java.util.List;

import org.exoplatform.commons.utils.ListAccess;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 * @version $Id$
 * 
 */
public class SerializablePageList<E> extends StatelessPageList<E> {

	private final ListAccess<E> listAccess;

	public SerializablePageList(ListAccess<E> listAccess, int pageSize) {
		super(pageSize);

		//
		this.listAccess = listAccess;
	}

	public SerializablePageList(Class<E> serializableType, List<E> list, int pageSize) {
		super(pageSize);

		//
		this.listAccess = new ListAccessImpl<E>(serializableType, list);
	}

	@Override
	protected ListAccess<E> connect() throws Exception {
		return listAccess;
	}
}
