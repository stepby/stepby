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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.List;

import org.exoplatform.commons.utils.LazyList;
import org.exoplatform.commons.utils.ListAccess;
import org.exoplatform.commons.utils.PageList;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 * @version $Id$
 *
 */
@SuppressWarnings("deprecation")
public abstract class AbstractSerializablePageList<E> extends PageList<E>{
	
	private static final Field pageSizeField;
	
	static {
		try {
			pageSizeField = PageList.class.getDeclaredField("pageSize_");
			pageSizeField.setAccessible(true);
		}
		catch (NoSuchFieldException e) {
			throw new Error(e);
		}
	}

	public AbstractSerializablePageList(int pageSize) {
		super(pageSize);
	}
	
	protected AbstractSerializablePageList() {
		super(10);
	}
	
	private LazyList<E> lazyList;
	
	protected abstract ListAccess<E> connect() throws Exception;
	
	private void ensureCorrectState() {
		if(lazyList == null) {
			try {
				lazyList = new LazyList<E>(connect(), super.getPageSize());
			} catch(Exception e) {
				throw new UndeclaredThrowableException(e);
			}
		}
		
		int currentPage = currentPage_;
		
		super.setAvailablePage(lazyList.size());
		
		if(currentPage != -1) {
			currentPage_ = currentPage;
		}
	}
	
	@Override
	protected final void populateCurrentPage(int page) throws Exception {
		ensureCorrectState();
		int from = getFrom();
		int to = getTo();
		currentListPage_ = lazyList.subList(from, to);
	}
	
	@Override
	public final List<E> getAll() {
		ensureCorrectState();
		return lazyList;
	}
	
	protected final void writeState(ObjectOutputStream out) throws IOException {
		
	}
	
	protected final void readSate(ObjectInputStream is) throws IOException {
		
	}
	
	@Override
	public final int getAvailablePage() {
		ensureCorrectState();
		return super.getAvailablePage();
	}
	
	@Override
	public final int getTo() {
		ensureCorrectState();
		return getTo();
	}
	
	@Override
	public final int getFrom() {
		ensureCorrectState();
		return getFrom();
	}
	
	@Override
	protected final void setAvailablePage(int available) {
		ensureCorrectState();
		super.setAvailablePage(available);
	}
	
	@Override
	protected final void checkAndSetPage(int page) throws Exception {
		ensureCorrectState();
		super.checkAndSetPage(page);
	}
	
	@Override
	public final List<E> getPage(int page) throws Exception {
		ensureCorrectState();
		return super.getPage(page);
	}
	
	@Override
	public final List<E> currentPage() throws Exception {
		ensureCorrectState();
		return super.currentPage();
	}
	
	@Override
	public final int getAvailable() {
		ensureCorrectState();
		return super.getAvailable();
	}
	
	@Override
	public final int getCurrentPage() {
		ensureCorrectState();
		return super.getCurrentPage();
	}
	
	@Override
	public final void setPageSize(int pageSize) {
		ensureCorrectState();
		super.setPageSize(pageSize);
	}
	
	@Override
	public final int getPageSize() {
		ensureCorrectState();
		return super.getPageSize();
	}
}
