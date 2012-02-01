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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 * @version $Id$
 *
 */
public class ApplicationResourceResolver extends ResourceResolver {
	
	protected static Log log = ExoLogger.getLogger("portal:ApplicationResourceResolver");
	
	private Map<String, ResourceResolver> resources_ = new HashMap<String, ResourceResolver>();
	
	public ApplicationResourceResolver() {
		addResourceResolver(new FileResourceResolver());
		addResourceResolver(new ClasspathResourceResolver());
	}
	
	public ResourceResolver getResourceResolverByScheme(String scheme) {
		return resources_.get(scheme);
	}
	
	public ResourceResolver getResourceResolver(String url) {
		String scheme = "app:";
		int index = url.indexOf(":");
		if(index > 0) scheme = url.substring(0, index + 1);
		if(log.isDebugEnabled()) log.debug("Try to extract resource resolver for the url " + url);
		return resources_.get(scheme);
	}
	
	public void addResourceResolver(ResourceResolver resolver) {
		if(log.isDebugEnabled()) log.debug("Add resource resovler for scheme" + resolver.getClass() + " : " + resolver.getResourceScheme());
		resources_.put(resolver.getResourceScheme(), resolver);
	}

	@Override
	public URL getResource(String url) throws Exception {
		return getResourceResolver(url).getResource(url);
	}

	@Override
	public InputStream getInputStream(String url) throws Exception {
		return getResourceResolver(url).getInputStream(url);
	}

	@Override
	public List<URL> getResources(String url) throws Exception {
		return getResourceResolver(url).getResources(url);
	}

	@Override
	public List<InputStream> getInputStreams(String url) throws Exception {
		return getResourceResolver(url).getInputStreams(url);
	}

	@Override
	public String getResourceScheme() {
		return "app:";
	}

	@Override
	boolean isModified(String url, long lastAccess) {
		return getResourceResolver(url).isModified(url, lastAccess);
	}
	
	public boolean isResovlable(String url) {
		return getResourceResolver(url) != null;
	}
	
	public String getResourceIdPrefix() {
		return Integer.toString(hashCode());
	}
}
