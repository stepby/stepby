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

import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 * @version $Id$
 *
 */
public class TestDOMSerializer extends TestCase {
	
	public void testScriptNoAttribute() throws Exception {
		assertSerialization("<script></script>", "<script />");
	}
	
	public void testScriptWithAttribute() throws Exception {
		assertSerialization("<script type=\"text/javascript\"></script>", "<script type='text/javascript' />");
	}
	
	public void testMetaNoAttribute() throws Exception {
		assertSerialization("<meta/>","<meta />");
	}
	
	public void testMetaWithAttribute() throws Exception {
		assertSerialization("<meta http-equiv=\"Content-type\"/>", "<meta http-equiv='Content-type'></meta>");
	}
	
	public void testWithCDATA() throws Exception {
		assertSerialization("<content><![CDATA[Hello > world]]></content>", "<content><![CDATA[Hello > world]]></content>");
	}
	
	public void testWithCDATA2() throws Exception {
		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		Element elt = doc.createElement("content");
		elt.setAttribute("type", "text/plain");
		CDATASection cdata = doc.createCDATASection("Hello > world");
		elt.appendChild(cdata);
		
		StringWriter writer = new StringWriter();
		DOMSerializer.serialize(elt, writer);
		assertEquals("<content type=\"text/plain\"><![CDATA[Hello > world]]></content>", writer.toString());
	}
	
	public void testWithText() throws Exception {
		assertSerialization("<content>foo copy bar</content>", "<content>foo © bar</content>");
	}

	private void assertSerialization(String expectedMarkup, String markup) throws Exception {
		Element elt = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(markup))).getDocumentElement() ;
		StringWriter writer = new StringWriter();
		DOMSerializer.serialize(elt, writer);
		assertEquals(expectedMarkup, writer.toString());
	}
}
