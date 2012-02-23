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

import org.chromattic.api.Chromattic;
import org.chromattic.api.ChromatticException;
import org.chromattic.api.ChromatticSession;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 * @version $Id$
 *
 * A specific implementation of the {@link org.chromattic.api.Chromattic} interface that delegates the obtaining of a session to managed system.
 */
public class ChromatticImpl implements Chromattic {

	private final ChromatticLifeCycle configurator;
	
	public ChromatticImpl(ChromatticLifeCycle configurator) {
		this.configurator = configurator;
	}

	@Override
	public ChromatticSession openSession() throws ChromatticException {
		SessionContext context = configurator.getContext(false);
		if(context == null) context = configurator.openSynchronizedContext();
		return context.getSession();
	}

	@Override
	public ChromatticSession openSession(String workspace) throws ChromatticException {
		throw new UnsupportedOperationException();
	}

	@Override
	public ChromatticSession openSession(Credentials credentials, String workspace) throws ChromatticException {
		throw new UnsupportedOperationException();
	}

	@Override
	public ChromatticSession openSession(Credentials credentials) throws ChromatticException {
		throw new UnsupportedOperationException();
	}
}
