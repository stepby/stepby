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
package org.exoplatform.component.test;

import java.util.Map;

import org.gatein.common.logging.Logger;
import org.gatein.common.logging.LoggerFactory;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 * @version $Id$
 *
 */
public abstract class AbstractGateInTest extends BaseGateInTest {

	protected final Logger log = LoggerFactory.getLogger(getClass());
	
	protected AbstractGateInTest() {
		
	}
	
	protected AbstractGateInTest(String name) {
		super(name);
	}
	
	protected void beforeRunBare() throws Exception {
		
	}
	
	protected void afterRunBare() {
		
	}
	
	@Override
	public final void runBare() throws Throwable {
		if("org.hsqldb.jdbcDriver".equals(System.getProperty("gatein.test.datasource.driver"))) {
			System.setProperty("gatein.test.datasource.password", "");
		}
		
		log.info("Running unit test: " + getName());
		for(Map.Entry<?, ?>  entry : System.getProperties().entrySet()) {
			if(entry.getKey() instanceof String) {
				String key = (String) entry.getKey();
				log.debug(key + " = " + entry.getValue());
			}
		}
		
		beforeRunBare();
		
		try {
			super.runBare();
			log.info("Unit test " + getName() + " completed");
		} catch(Throwable t) {
			log.error("Unit test " + getName() + " did not complete", t);
			throw t;
		} finally {
			afterRunBare();
		}
	}
}
