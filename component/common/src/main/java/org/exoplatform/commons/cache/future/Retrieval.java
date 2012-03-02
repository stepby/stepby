/*
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
package org.exoplatform.commons.cache.future;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 * @version $Id$
 *
 * Mar 1, 2012
 */
public class Retrieval<K, V, C> implements Callable<V> {
	
	private final C context;
	
	private final K key;
	
	private final FutureCache<K, V, C> cache;
	
	final FutureTask<V> future;
	
	transient Thread current;
	
	public Retrieval(C context, K key, FutureCache<K, V, C> cache) {
		this.key = key;
		this.context = context;
		this.future = new FutureTask<V>(this);
		this.cache = cache;
		this.current = null;
	}

	@Override
	public V call() throws Exception {
		//Retrieval value from loader
		V value = cache.loader.retrieve(context, key);
		if(value != null) {
			//Cache it, it is made available to other threads (unless someone removes it)
			cache.put(key, value);
			return value;
		}
		return null;
	}
}
