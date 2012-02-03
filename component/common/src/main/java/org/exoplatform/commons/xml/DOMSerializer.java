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

import java.io.IOException;
import java.io.Writer;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.exoplatform.commons.util.HTMLEntityEncoder;
import org.gatein.common.logging.Logger;
import org.gatein.common.logging.LoggerFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 * @version $Id$
 *
 */
public class DOMSerializer {
	
	private static final Logger log = LoggerFactory.getLogger(DOMSerializer.class);
	
	private static final XMLOutputFactory outputFactory;
	
	private static final String DEFAULT_XML_OUTPUT_FACTORY = "com.sun.xml.internal.stream.XMLOutputFactoryImpl";
	
	static {
		XMLOutputFactory tmp;
		try {
			Class<XMLOutputFactory> cl = (Class<XMLOutputFactory>)Thread.currentThread().getContextClassLoader().loadClass(DEFAULT_XML_OUTPUT_FACTORY);
			tmp = cl.newInstance();
		}
		catch (Exception e) {
			tmp = XMLOutputFactory.newInstance();
			log.warn("Could not instantiate " + DEFAULT_XML_OUTPUT_FACTORY + " will use default provided by runtime instead " +
		            tmp.getClass().getName());
		}
		
		outputFactory = tmp;
	}
	
	public static void serialize(Element element, Writer writer) throws IOException, XMLStreamException {
		XMLStreamWriter xml = outputFactory.createXMLStreamWriter(writer);
		serialize(element, xml);
		xml.writeEndDocument();
		xml.flush();
	}
	
	private static void serialize(Element element, XMLStreamWriter writer) throws IOException, XMLStreamException {
		String tagName = element.getTagName();
		boolean empty;
		if(tagName.equalsIgnoreCase("script")) {
			empty= false;
		} else {
			empty = true;
			NodeList children = element.getChildNodes();
			int length = children.getLength();
			for(int i = 0; i < length && empty; i++) {
				Node child = children.item(i);
				if(child instanceof CharacterData) {
					empty = false;
				} else if(child instanceof Element) {
					empty = false;
				}
			}
		}
		
		if(empty) writer.writeEmptyElement(tagName);
		else writer.writeStartElement(tagName);
		
		if(element.hasAttributes()) {
			NamedNodeMap attrs = element.getAttributes();
			int length = attrs.getLength();
			for(int i = 0; i < length; i++) {
				Attr attr = (Attr)attrs.item(i);
				writer.writeAttribute(attr.getName(), attr.getValue());
			}
		}
		
		if(!empty) {
			NodeList children = element.getChildNodes();
			int length = children.getLength();
			for(int i = 0; i < length; i++) {
				Node child = children.item(i);
				if(child instanceof CDATASection) {
					writer.writeCData(((CDATASection) child).getData());
				} else if (child instanceof CharacterData) {
					writeTextData(writer, ((CharacterData) child).getData());
				} else if(child instanceof Element) {
					serialize((Element) child, writer);
				}
			}
		}
		
		writer.writeEndElement();
	}
	
	private static void writeTextData(XMLStreamWriter writer, String data) throws XMLStreamException {
		StringBuilder b = new StringBuilder();
		
		for(int i = 0; i < data.length(); i++) {
			char c = data.charAt(i);
			String encodedValue = HTMLEntityEncoder.getInstance().lookupEntityName(c);
			if(encodedValue == null) b.append(c);
			else b.append(encodedValue);
			writer.writeCharacters(b.toString());
		}
	}
}
