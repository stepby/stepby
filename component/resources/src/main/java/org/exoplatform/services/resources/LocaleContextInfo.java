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

import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.chromattic.api.annotations.AutoCreated;
import org.exoplatform.commons.util.I18N;

/**
 * Data structure that holds the inputs for {@link LocalePolicy} pluggable policies mechanism.
 * 
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 * @version $Id$
 *
 * Mar 7, 2012
 */
public class LocaleContextInfo {

	private Set<Locale> supportedLocales;
	private List<Locale> browserLocales;
	private List<Locale> cookieLocales;
	private Locale userProfileLocale;
	private String remoteUser;
	private Locale portalLocale;
	private Locale sessionLocale;
	private Locale requestLocale;

	public Locale getUserProfileLocale() {
		return userProfileLocale;
	}

	public void setUserProfileLocale(Locale userProfileLocale) {
		this.userProfileLocale = userProfileLocale;
	}

	public String getRemoteUser() {
		return remoteUser;
	}

	public void setRemoteUser(String remoteUser) {
		this.remoteUser = remoteUser;
	}

	public Locale getPortalLocale() {
		return portalLocale;
	}

	public void setPortalLocale(Locale portalLocale) {
		this.portalLocale = portalLocale;
	}

	public Locale getSessionLocale() {
		return sessionLocale;
	}

	public void setSessionLocale(Locale sessionLocale) {
		this.sessionLocale = sessionLocale;
	}

	public Locale getRequestLocale() {
		return requestLocale;
	}

	public void setRequestLocale(Locale requestLocale) {
		this.requestLocale = requestLocale;
	}

	public Set<Locale> getSupportedLocales() {
		return supportedLocales;
	}

	public void setSupportedLocales(Set<Locale> supportedLocales) {
		this.supportedLocales = supportedLocales;
	}

	public List<Locale> getBrowserLocales() {
		return browserLocales;
	}

	public void setBrowserLocales(List<Locale> browserLocales) {
		this.browserLocales = browserLocales;
	}

	public List<Locale> getCookieLocales() {
		return cookieLocales;
	}

	public void setCookieLocales(List<Locale> cookieLocales) {
		this.cookieLocales = cookieLocales;
	}
	
	public Locale getLocaleIfSupported(Locale locale) {
		if(locale == null) return null;
		if(supportedLocales.contains(locale)) return locale;
		return null;
	}
	
	public Locale getLocaleIfLangSupported(Locale locale) {
		Locale lc = getLocaleIfLangSupported(locale);
		if(lc == null && !locale.getCountry().isEmpty()) {
			lc = new Locale(locale.getLanguage());
			return getLocaleIfLangSupported(lc);
		}
		return lc;
	}
	
	public static Locale getLocale(String portalLocaleName) {
		try {
			return I18N.parseJavaIdentifier(portalLocaleName);
		} catch(Exception e) {
			return null;
		}
	}

	/**
	 *  instead of locale.toString()
	 *  
	 * @deprecated
	 */
	public static String getLocaleAsString(Locale locale) {
		return I18N.toJavaIdentifier(locale);
	}
}
