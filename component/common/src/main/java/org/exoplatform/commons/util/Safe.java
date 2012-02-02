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

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Set;

import org.gatein.common.io.IOTools;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 * @version $Id$
 *
 */
public class Safe {

	private Safe() 
	{
	}
	
	public static boolean equals(Object o1, Object o2) {
		if(o1 == null) return o2 == null;
		else return o2 != null && o1.equals(o2);
	}
	
	public static boolean close(Closeable closeable) {
		if(closeable != null) {
			try {
				closeable.close();
				return true;
			} catch(IOException ignore) {
			} catch(RuntimeException ignore) {
			}
		}
		return false;
	}
	
	public static byte[] getBytes(InputStream is) {
		byte[] bytes;
		if(is == null) return null;
		try {
			bytes = IOTools.getBytes(is);
			return bytes;
		} catch(IOException ignore) {
			return null;
		} finally {
			IOTools.safeClose(is);
		}
	}
	
	public static <E> Set<E> unmodifiableSet(Set<E> set) {
		if(set == null) return null;
		return Collections.unmodifiableSet(set);
	}
}
