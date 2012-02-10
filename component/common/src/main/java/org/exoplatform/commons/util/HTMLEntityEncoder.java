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

import java.io.StringWriter;
import java.io.Writer;

import org.gatein.common.io.WriterCharWriter;
import org.gatein.common.text.CharWriter;
import org.gatein.common.text.EncodingException;
import org.gatein.common.text.EntityEncoder;
import org.gatein.common.util.ParameterValidation;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 * @version $Id$
 *
 */
public class HTMLEntityEncoder extends EntityEncoder {
	
	private static volatile HTMLEntityEncoder singletonInstance;
	
	private final String[] hexToEntity = buildHexEntityNumberArray();
	
	private static final char[] IMMUNE_HTML =  { ',', '.', '-', '_', ' ' };
	
	private static final char[] IMMUNE_HTMLATTR = { ',', '.', '-', '_' };
	
	public static HTMLEntityEncoder getInstance() {
		if(singletonInstance == null) {
			synchronized (HTMLEntityEncoder.class) {
				if(singletonInstance == null) {
					singletonInstance = new HTMLEntityEncoder();
				}
			}
		}
		return singletonInstance;
	}
	
	public String encodeHTML(String input) {
		return encode(input, IMMUNE_HTML);
	}
	
	public String encodeHTMLAttribute(String input) {
		return encode(input, IMMUNE_HTMLATTR);
	}
	
	@Override
	public void safeEncode(char[] chars, int off, int len, CharWriter writer) throws EncodingException {
		safeEncode(chars, off, len, writer, IMMUNE_HTML);
	}
	
	public final String lookupEntityName(char c) {
		return lookup(c);
	}
	
	public final String lookupHexEntityNumber(char c) {
		if(c < 0xFF) {
			return hexToEntity[c];
		}
		return Integer.toHexString(c);
	}
	
	private boolean isImmutable(char[] immune, char c) {
		for(char ch : immune) {
			if(ch == c) return true;
		}
		return false;
	}
	
	private void safeEncode(char[] chars, int off, int len, CharWriter writer, char[] immune) throws EncodingException {
		int previous = off;
		int to = off +  len;
		
		for(int current = off; current < to; current++) {
			char c = chars[current];
			if(isImmutable(immune, c)) continue;
			
			String replacement;
			String hex;
			
			if((replacement = lookupEntityName(c)) != null) {
				writer.append(chars, previous, current - previous);
				writer.append('&').append(replacement).append(';');
				previous = current + 1;
			} else if((hex = lookupHexEntityNumber(c)) != null) {
				writer.append(chars, previous, current - previous);
				writer.append("&#x").append(hex).append(';');
				previous = current + 1;
			}
		}
		writer.append(chars, previous, chars.length - previous);
	}
	
	private String encode(String input, char[] immutable) {
		ParameterValidation.throwIllegalArgExceptionIfNull(input, "String");
		
		Writer w = new StringWriter();
		CharWriter charWriter = new WriterCharWriter(w);
		safeEncode(input.toCharArray(), 0, input.length(), charWriter, immutable);
		return w.toString();
	}
	
	private String[] buildHexEntityNumberArray() {
		String[] array = new String[256];
		for(char c = 0; c < 0xFF; c++) {
			// (c >= 0 && c <= 9 || c >= A && c <= Z || c >= a && c <= z)
			if(c >= 0x30 && c <= 0x39 || c >= 0x41 && c <= 0x5A || c >= 0x61 && c <= 0x7A) { 
				array[c] = null;
			} else {
				array[c] = Integer.toHexString(c);
			}
		}
		return array;
	}
}
