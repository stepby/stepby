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
package org.exoplatform.services.resources;

import java.util.Locale;

/**
 * This interface represents a pluggable mechanism for different locale determining algorithms.
 * 
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 * @version $Id$
 *
 * Mar 7, 2012
 */
public interface LocalePolicy {

	/**
	 * Determine the locale to be used for current request
	 * 
	 * @param localeContext the locale context info available to implementations as inputs to use when determining appropriate Locale
	 * @return Locale to be used for current user's request
	 */
	public Locale determineLocale(LocaleContextInfo localeContext);
}
