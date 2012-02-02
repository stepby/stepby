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
package org.exoplatform.commons.util;

import java.util.Locale;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 * @version $Id$
 *
 */
public class I18N {
	
	public static String toJavaIdentifier(Locale locale) throws NullPointerException {
		if(locale == null) throw new NullPointerException("No null locale accepted");
		return locale.toString();
	}
	
	private static boolean isLetter(char c) {
		return c >='A' && c <= 'Z' || c >= 'a' && c <= 'z';
	}
	
	public static Locale parseJavaIdentifier(String s) throws NullPointerException, IllegalArgumentException {
		if(s == null) throw new IllegalArgumentException("Empty locale");
		
		char c0 = s.charAt(0);
		if(c0 == '_') {
			return parseCountry("", s, 0);
		} else if(!isLetter(c0) || s.length() < 2 || !isLetter(s.charAt(1))) {
			throw new IllegalArgumentException();
		} else {
			return parseCountry(s.substring(0, 2), s, 2);
		}
	}
	
	private static Locale parseCountry(String lang, String s, int index) throws IllegalArgumentException {
		if(s.length() == index) return new Locale(lang);
		else if(s.charAt(index) == '_' || s.length() < index + 3) throw new IllegalArgumentException();
		else {
			char c0 = s.charAt(index + 1);
			if(c0 == '_') {
				if(lang.length() == 0) throw new IllegalArgumentException();
				else return parseVariant(lang, "", s, index + 1);
			} else if(!isLetter(c0) || !isLetter(s.charAt(index + 2))) {
				throw new IllegalArgumentException();
			} else {
				return parseVariant(lang, s.substring(index + 1, index + 3), s, index + 3);
			}
		}
	}
	
	private static Locale parseVariant(String lang, String country, String s, int index) throws IllegalArgumentException {
		if(s.length() == index) {
			return new Locale(lang, country);
		} else if(s.charAt(index) != '_' || s.length() < index + 2) {
			throw new IllegalArgumentException();
		} else {
			for(int i = index; i < s.length(); i++) {
				if(!isLetter(s.charAt(i))) throw new IllegalArgumentException();
			}
			String variant = s.substring(index + 1);
			return new Locale(lang, country, variant);
		}
	}
}
