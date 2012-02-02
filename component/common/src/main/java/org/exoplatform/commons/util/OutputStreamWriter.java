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

import org.gatein.common.io.UndeclaredIOException;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 * @version $Id$
 *
 */
public class OutputStreamWriter extends Printer implements BinaryOutput {
	
	private final TextEncoder encoder;
	
	private final OutputStream out;
	
	private final IOFailureFlow failureFlow;
	
	private final boolean ignoreOnFailure;
	
	private boolean failed;
	
	private final boolean flushOnClose;
	
	public OutputStreamWriter(
		TextEncoder encoder,
		OutputStream out,
		IOFailureFlow failureFlow,
		boolean ignoreOnFailure,
		boolean flushOnClose,
		int bufferSize,
		boolean growing)  throws IllegalArgumentException {
		
		if(encoder == null) throw new IllegalArgumentException("No null encoder accepted");
		if(out == null) throw new IllegalArgumentException("No null output stream accepted");
		if(failureFlow == null) throw new IllegalArgumentException("No null control flow accepted");
		if(bufferSize < 0) throw new IllegalArgumentException("Invalid negative max buffer size: " + bufferSize);
		
		if(!growing) out = new BufferingOutputStream(out, bufferSize);
		else out = new GrowingOutputStream(out,bufferSize);
		
		this.encoder = encoder;
		this.out = out;
		this.failureFlow = failureFlow;
		this.failed = false;
		this.ignoreOnFailure = ignoreOnFailure;
		this.flushOnClose = flushOnClose;
	}
	
	/**
	 * @see org.exoplatform.commons.util.BinaryOutput#getCharset()
	 */
	@Override
	public Charset getCharset() {
		return encoder.getCharset();
	}
	
	public final IOFailureFlow getFailureFlow() {
		return failureFlow;
	}
	
	public final boolean isIgnoreOnFailure() {
		return ignoreOnFailure;
	}
	
	public final boolean isFailed() {
		return failed;
	}

	/**
	 * @see org.exoplatform.commons.util.BinaryOutput#write(byte)
	 */
	@Override
	public final void write(byte b) throws IOException {
		if(!failed) {
			try {
				out.write(b);
			} catch(IOException e) {
				handle(e);
			}
		}
	}

	/**
	 * @see org.exoplatform.commons.util.BinaryOutput#write(byte[])
	 */
	@Override
	public final void write(byte[] bytes) throws IOException {
		if(!failed) {
			try {
				out.write(bytes);
			} catch(IOException e) {
				handle(e);
			}
		}
	}

	/**
	 * @see org.exoplatform.commons.util.BinaryOutput#write(byte[], int, int)
	 */
	@Override
	public final void write(byte[] bytes, int off, int length) throws IOException {
		if(!failed) {
			try {
				out.write(bytes, off, length);
			} catch(IOException e) {
				handle(e);
			}
		}
	}

	/**
	 * @see java.io.Writer#write(char[], int, int)
	 */
	@Override
	public final void write(char[] cbuf, int off, int len) throws IOException {
		if(!failed) {
			try {
				encoder.encode(cbuf, off, len, out);
			} catch(IOException e) {
				handle(e);
			}
		}
	}
	
	@Override
	public final void write(int c) throws IOException {
		if(!failed) {
			try {
				encoder.encode((char)c, out);
			} catch(IOException e) {
				handle(e);
			}
		}
	}
	
	@Override
	public final void write(char[] cbuf) throws IOException {
		if(!failed) {
			try {
				encoder.encode(cbuf, 0, cbuf.length, out);
			} catch(IOException e) {
				handle(e);
			}
		}
	}
	
	@Override
	public final void write(String str) throws IOException {
		if(!failed) {
			try {
				encoder.encode(str, 0, str.length(), out);
			} catch(IOException e) {
				handle(e);
			}
		}
	}
	
	@Override
	public final void write(String str, int off, int len) throws IOException {
		if(!failed) {
			try {
				encoder.encode(str, off, len, out);
			} catch(IOException e) {
				handle(e);
			}
		}
	}

	/**
	 * @see java.io.Writer#flush()
	 */
	@Override
	public final void flush() throws IOException {
		if(!failed && !flushOnClose) {
			try {
				out.flush();
			} catch(IOException e) {
				handle(e);
			}
		}
	}

	/**
	 * @see java.io.Writer#close()
	 */
	@Override
	public final void close() throws IOException {
		try {
			out.close();
		} catch(IOException e) {
			handle(e);
		}
	}
	
	private void handle(IOException e) throws IOException {
		if(ignoreOnFailure) {
			failed = true;
		}
		switch(failureFlow) {
			case IGNORE:
				break;
			case THROW_UNDECLARED:
				throw new UndeclaredIOException(e);
			case RETHROW:
				throw e;
		}
	}
	
	public void flushOutputStream() throws IOException {
		if(!failed) {
			try {
				out.flush();
			} catch(IOException e){
				handle(e);
			}
		}
	}
}
