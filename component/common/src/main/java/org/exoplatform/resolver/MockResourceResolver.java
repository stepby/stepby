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
package org.exoplatform.resolver;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 * @version $Id$
 *
 */
public class MockResourceResolver extends ResourceResolver {
	
	private Map<String, URL> resources_;
	
	private MockResourceResolver(Map<String, URL> mapResources) {
		this.resources_ = mapResources;
	}

	@Override
	public URL getResource(String url) throws Exception {
		return resources_.get(url);
	}

	@Override
	public InputStream getInputStream(String url) throws Exception {
		URL result = resources_.get(url);
		if(result != null) return result.openStream();
		return null;
	}

	@Override
	public List<URL> getResources(String url) throws Exception {
		return null;
	}

	@Override
	public List<InputStream> getInputStreams(String url) throws Exception {
		return null;
	}

	@Override
	public String getResourceScheme() {
		return null;
	}

	@Override
	boolean isModified(String url, long lastAccess) {
		return false;
	}
}
