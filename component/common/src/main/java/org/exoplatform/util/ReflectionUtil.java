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
package org.exoplatform.util;

import java.lang.ref.SoftReference;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.hibernate.cfg.annotations.SetBinder;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 * @version $Id$
 *
 */
public class ReflectionUtil
{
	
	static public Object[] EMPTY_ARGS = {};
	
	static private HashMap<String, SoftReference<Method>> 	getMethodCache_ = new HashMap<String, SoftReference<Method>>();
	
	static private HashMap<String, SoftReference<Method>> 	setMethodCache_ = new HashMap<String, SoftReference<Method>>();
	
	static public Method getGetBindingMethod(Object bean, String bindingField) throws Exception {
		String key = bindingField + "@" + bean.getClass();
		Method method = null;
		SoftReference<Method> sref = getMethodCache_.get(key);
		if(sref != null) method = sref.get();
		else {
			Exception exp = null;
			try {
				method = getBindingMethod(bean, bindingField, new Class[]{}, "get");
			}
			catch(Exception e) {
				exp = e;
			}
			
			if(method == null) {
				try  {
					method = getBindingMethod(bean, bindingField, new Class[]{}, "is");
				} catch(Exception e) {
					exp = e;
				}
			}
			if(method == null && exp != null) throw exp;
			getMethodCache_.put(key, new SoftReference<Method>(method));
		}
		return method;
	}
	
	static public Method getSetBindingMethod(Object bean, String bindingField, Class[] classes) throws Exception {
		String key = bindingField + "@" + bean.getClass();
		Method method = null;
		SoftReference<Method> sref = setMethodCache_.get(key);
		if(sref != null) method = sref.get();
		else {
				method = getBindingMethod(bean, bindingField, new Class[] {}, "set");
				setMethodCache_.put(key, new SoftReference<Method>(method));
		}
		return method;
	}
	
	static private Method getBindingMethod(Object bean, String bindingField, Class[] classes, String prefix) throws Exception
	{
		StringBuilder b = new StringBuilder();
		b.append(prefix);
		b.append(Character.toUpperCase(bindingField.charAt(0)));
		b.append(bindingField.substring(1));
		return bean.getClass().getMethod(b.toString(), classes);
	}
}
