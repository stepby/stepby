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
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 * @version $Id$
 *
 */
public class OutputStreamWriter extends Printer implements BinaryOutput {
	
	public OutputStreamWriter(
		TextEncoder encoder,
		OutputStream out,
		IOFailureFlow failureFlow,
		boolean ignoreOnFailure,
		boolean flushOnClose,
		int bufferSize,
		boolean growing)  throws IllegalArgumentException {
		
	}

	/**
	 * @see org.exoplatform.commons.util.BinaryOutput#getCharset()
	 */
	@Override
	public Charset getCharset() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.exoplatform.commons.util.BinaryOutput#write(byte)
	 */
	@Override
	public void write(byte b) throws IOException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @see org.exoplatform.commons.util.BinaryOutput#write(byte[])
	 */
	@Override
	public void write(byte[] bytes) throws IOException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @see org.exoplatform.commons.util.BinaryOutput#write(byte[], int, int)
	 */
	@Override
	public void write(byte[] bytes, int off, int length) throws IOException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @see java.io.Writer#write(char[], int, int)
	 */
	@Override
	public void write(char[] cbuf, int off, int len) throws IOException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @see java.io.Writer#flush()
	 */
	@Override
	public void flush() throws IOException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @see java.io.Writer#close()
	 */
	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub
		
	}

}
