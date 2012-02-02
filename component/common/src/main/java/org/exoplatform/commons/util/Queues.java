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

import java.util.AbstractQueue;
import java.util.Iterator;

import org.gatein.common.xml.NoSuchElementException;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 * @version $Id$
 *
 */
public class Queues {

	private static class LIFO<E> extends AbstractQueue<E> {
		
		private Object[] elements;
		
		private int size;
		
		public LIFO(int initialCapacity) {
			this.elements = new Object[initialCapacity];
			this.size = 0;
		}
		
		@Override
		public boolean offer(E e) {
			int length = elements.length;
			if(size == length) {
				Object[] tmp = new Object[(length * 3)/2 + 1];
				System.arraycopy(elements, 0, tmp, 0, length);
				elements = tmp;
			}
			elements[size++] = e;
			return true;
		}

		@Override
		public E poll() {
			if(size > 0) {
				int index = --size;
				@SuppressWarnings("unchecked")
				E element = (E) elements[index];
				elements[index] = null;
				return element;
			}
			return null;
		}

		@Override
		public E peek() {
			if(size > 0) {
				@SuppressWarnings("unchecked")
				E element = (E) elements[size - 1];
				return element;
			}
			return null;
		}

		@Override
		public Iterator<E> iterator() {
			return new Iterator<E>() {
				int count = size;
				@Override
				public boolean hasNext() {
					return count > 0;
				}

				@Override
				public E next() {
					if(!hasNext()) {
						throw new NoSuchElementException();
					}
					@SuppressWarnings("unchecked")
					E e = (E) elements[--count];
					return e;
				}

				@Override
				public void remove() {
					throw new UnsupportedOperationException();
				}
			};
		}

		@Override
		public int size() {
			return size;
		}
	}
}
