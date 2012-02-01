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

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 * @version $Id$
 *
 */
public class ServletResourceResolver extends ResourceResolver {
	
	protected static Log log = ExoLogger.getLogger("portal:SevletResourceResolver");
	
	private ServletContext scontext_;
	
	private String scheme_;
	
	public ServletResourceResolver(ServletContext context, String scheme) {
		scontext_ = context;
		scheme_ = scheme;
	}

	@Override
	public URL getResource(String url) throws Exception {
		String path = removeScheme(url);
		return scontext_.getResource(path);
	}

	@Override
	public InputStream getInputStream(String url) throws Exception {
		String path = removeScheme(url);
		return scontext_.getResourceAsStream(path);
	}

	@Override
	public List<URL> getResources(String url) throws Exception {
		List<URL> list = new ArrayList<URL>();
		list.add(getResource(url));
		return list;
	}

	@Override
	public List<InputStream> getInputStreams(String url) throws Exception {
		List<InputStream> list = new ArrayList<InputStream>();
		list.add(getInputStream(url));
		return list;
	}
	
	public String getRealPath(String url) {
		String path = removeScheme(url);
		return scontext_.getRealPath(path);
	}

	@Override
	public String getResourceScheme() {
		return scheme_;
	}

	@Override
	boolean isModified(String url, long lastAccess) {
		File file = new File(getRealPath(url));
		if(log.isDebugEnabled())  log.debug(url + ": " + file.lastModified() + " " + lastAccess);
		if(file.exists() && file.lastModified() > lastAccess) return true;
		return false;
	}

	
	public String getWebAccessPath(String url) {
		if(log.isDebugEnabled()) log.debug("GET WEB ACCESS " + url);
		return "/" + scontext_.getServletContextName() + removeScheme(url);
	}
}
