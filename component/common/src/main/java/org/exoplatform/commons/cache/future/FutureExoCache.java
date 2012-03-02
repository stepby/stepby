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

import java.io.Serializable;

import org.exoplatform.services.cache.ExoCache;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 * @version $Id$
 *
 * Mar 2, 2012
 */
public class FutureExoCache<K extends Serializable, V, C> extends FutureCache<K, V, C> {
	
	private final ExoCache<K, V> cache;

	public FutureExoCache(Loader<K, V, C> loader, ExoCache<K, V> cache) {
		super(loader);
		this.cache = cache;
	}

	@Override
	protected V get(K key) {
		return cache.get(key);
	}

	@Override
	protected void put(K key, V value) {
		cache.put(key, value);
	}
}
