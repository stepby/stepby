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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 * @version $Id$
 *
 */
public class CharsetCharEncoder implements CharEncoder {
	
	private static final CharEncoder UTF8 = new CharsetCharEncoder(Charset.forName("UTF8"));

   public static CharEncoder getUTF8()
   {
      return UTF8;
   }
	
	private final Charset charset;
	
	public CharsetCharEncoder(Charset charset) {
		this.charset = charset;
	}

	/**
	 * @see org.exoplatform.commons.util.CharEncoder#encode(char)
	 */
	@Override
	public byte[] encode(char c) {
		CharsetEncoder encoder = charset.newEncoder();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		OutputStreamWriter writer = new OutputStreamWriter(baos, encoder);
		try {
			writer.write(c);
			writer.close();
			return baos.toByteArray();
		} catch(IOException e) {
			throw new AssertionError(e);
		}
	}

	/**
	 * @see org.exoplatform.commons.util.CharEncoder#getCharset()
	 */
	@Override
	public Charset getCharset() {
		return charset;
	}
}
