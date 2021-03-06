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
package org.exoplatform.commons.serialization.model.metadata;

import org.exoplatform.commons.serialization.api.TypeConverter;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 * @version $Id$
 *
 */
public class ConvertedTypeMetaData extends TypeMetaData {
	
	private final Class<? extends TypeConverter<?, ?>> converterClass;

	public ConvertedTypeMetaData(String name, Class<? extends TypeConverter<?, ?>> converterClass) {
		super(name);
		if(converterClass == null) throw new NullPointerException();
		this.converterClass = converterClass;
	}
	
	public Class<? extends TypeConverter<?, ?>> getConverterClass() {
		return converterClass;
	}
	
	@Override
	public String toString() {
		return "ConverterMetaData[name = " + name + ", converterClass = " + converterClass + "]"; 
	}
}
