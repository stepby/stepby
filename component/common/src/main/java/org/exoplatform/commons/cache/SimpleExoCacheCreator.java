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
package org.exoplatform.commons.cache;

import java.io.Serializable;

import org.exoplatform.services.cache.ExoCache;
import org.exoplatform.services.cache.ExoCacheConfig;
import org.exoplatform.services.cache.ExoCacheInitException;
import org.exoplatform.services.cache.concurrent.ConcurrentFIFOExoCache;
import org.exoplatform.services.cache.impl.jboss.ExoCacheCreator;
import org.jboss.cache.Cache;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 * @version $Id$
 *
 * Mar 2, 2012
 */
public class SimpleExoCacheCreator implements ExoCacheCreator {

	@Override
	public ExoCache<Serializable, Object> create(ExoCacheConfig config, Cache<Serializable, Object> cache) throws ExoCacheInitException {
		ExoCache<Serializable, Object> simple = new ConcurrentFIFOExoCache<Serializable, Object>();
		simple.setName(config.getName());
		simple.setLabel(config.getLabel());
		simple.setMaxSize(config.getMaxSize());
		simple.setLiveTime(config.getLiveTime());
		return simple;
	}

	@Override
	public Class<? extends ExoCacheConfig> getExpectedConfigType() {
		return ExoCacheConfig.class;
	}

	@Override
	public String getExpectedImplementation() {
		return "simple";
	}
}
