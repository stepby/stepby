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

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 * @version $Id$
 *
 */
public abstract class Text {

	public abstract void writeTo(Writer writer) throws IOException;
	
	public static Text create(byte[] bytes, Charset charset) throws IllegalArgumentException {
		return new Bytes(bytes, charset);
	}
	
	public static Text create(char[] chars) throws IllegalArgumentException {
		return new Chars(chars);
	}
	
	public static Text create(String s) throws IllegalArgumentException {
		return new Chars(s.toCharArray());
	}
	
	private static class Bytes extends Text {
		
		private final byte[] bytes;
		
		private final Charset charset;
		
		private volatile String s;
		
		private Bytes(byte[] bytes, Charset charset) {
			this.bytes = bytes;
			this.charset = charset;
		}
		
		@Override
		public void writeTo(Writer writer) throws IOException {
			if(writer instanceof BinaryOutput) {
				BinaryOutput out = (BinaryOutput)writer;
				if(charset.equals(out.getCharset())) {
					out.write(bytes);
					return;
				}
			}
			if(s == null) {
				s = new String(bytes, charset.name());
			}
			writer.write(s);
		}
	}
	
	private static class Chars extends Text {

		private int offset;
		
		private int count;
		
		private char[] chars;
		
		private Chars(char[] chars) {
			this.chars = chars;
			this.offset = 0 ;
			this.count = chars.length;
		}
		
		private Chars(char[] chars, int offset, int count) {
			this.chars = chars;
			this.offset = offset;
			this.count = count;
		}
		
		@Override
		public void writeTo(Writer writer) throws IOException {
			writer.write(chars, offset, count);
		}
		
		@Override
		public String toString() {
			return new String(chars, offset, count);
		}
	}
	
}