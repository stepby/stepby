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
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.gatein.common.logging.Logger;
import org.gatein.common.logging.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 * @version $Id$
 *
 */
public class XMLValidator {

	private final String[] schemas;
	
	private final ResourceEntityResolver resolver;
	
	private final Logger log = LoggerFactory.getLogger(XMLValidator.class);
	
	public XMLValidator(Class clazz, String systemId, String resourcePath) {
		schemas = new String[] { systemId };
		resolver = new ResourceEntityResolver(clazz, systemId, resourcePath);
	}
	
	public XMLValidator(Class clazz, Map<String, String> systemIdToResourcePath) {
		schemas = systemIdToResourcePath.keySet().toArray(new String[0]);
		resolver = new ResourceEntityResolver(clazz, systemIdToResourcePath);
	}
	
	public Document validate(DocumentSource source) throws NullPointerException, IOException {
		if(source == null) throw new NullPointerException();
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
		factory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaSource", schemas);
		factory.setNamespaceAware(true);
		factory.setValidating(true);
		
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			builder.setErrorHandler(new ValidatorReporter(log, source.getIdentifier()));
			builder.setEntityResolver(resolver);
			return builder.parse(source.getStream());
		} catch(ParserConfigurationException e) {
			log.error("Got a parser configuration exception when doing XSD validation");
			return null;
		} catch(SAXException e) {
			log.error("Got a sax exception when doing XSD validation");
			return null;
		}
	}
}
