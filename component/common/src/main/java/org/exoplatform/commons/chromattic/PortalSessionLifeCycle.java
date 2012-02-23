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
package org.exoplatform.commons.chromattic;

import javax.jcr.Credentials;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.chromattic.spi.jcr.SessionLifeCycle;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 * @version $Id$
 *
 * The implementaion of the {@link org.chromattic.spi.jcr.SessionLifeCycle} chromattic interface that
 *  delegates all the job to a {@link org.exoplatform.commons.chromattic.ChromatticLifeCycle} method
 */
public class PortalSessionLifeCycle implements SessionLifeCycle {
	
	final static ThreadLocal<ChromatticLifeCycle> bootContext = new ThreadLocal<ChromatticLifeCycle>();
	
	private final ChromatticLifeCycle chromatticLifeCycle;
	
	public PortalSessionLifeCycle() {
		chromatticLifeCycle = bootContext.get();
	}

	@Override
	public Session login() throws RepositoryException {
		return chromatticLifeCycle.doLogin();
	}
	
	@Override
	public void save(Session session) throws RepositoryException {
		session.save();
	}

	public void close(Session session) {
		
	}

	@Override
	public Session login(String workspace) throws RepositoryException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Session login(Credentials credentials, String workspace) throws RepositoryException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Session login(Credentials credentials) throws RepositoryException {
		throw new UnsupportedOperationException();
	}
}
