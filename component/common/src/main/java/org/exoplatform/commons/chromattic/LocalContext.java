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

import javax.jcr.RepositoryException;
import javax.jcr.Session;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 * @version $Id$
 *
 * A local context that is managed by thread local owned by chromattic life cycle instance.
 */
public class LocalContext extends AbstractContext {
	
	private Session jcrSession;
	
	LocalContext(ChromatticLifeCycle lifeCycle) {
		super(lifeCycle);
	}

	@Override
	public Session doLogin() throws RepositoryException {
		if(jcrSession != null) throw new IllegalStateException("Already logged in");
		jcrSession = openJCRSession();
		return jcrSession;
	}
	
	@Override
	public void close(boolean save) {
		try {
			super.close(save);
		} finally {
			if(jcrSession != null) jcrSession.logout();
		}
	}
}
