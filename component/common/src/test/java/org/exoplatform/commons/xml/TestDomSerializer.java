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
package org.exoplatform.commons.xml;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 * @version $Id$
 *
 */
public class TestDomSerializer extends TestCase {
	
	public void testWithCDATA() throws Exception {
		assertSerialization("<content><![CDATA[Hello > world]]></content>", "<content><![CDATA[Hello > world]]></content>");
	}
	
	public void testWithText() throws Exception {
		assertSerialization("<content>foo &copy; bar</content>", "<content>foo © bar</content>");
	}

	private void assertSerialization(String expectedMarkup, String markup) throws Exception {
		Element elt = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(markup))).getDocumentElement() ;
		StringWriter writer = new StringWriter();
		DOMSerializer.serialize(elt, writer);
		assertEquals(expectedMarkup, writer.toString());
	}
}
