/*
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
package org.sample.controllers;

import javax.inject.Inject;

import org.juzu.Action;
import org.juzu.Path;
import org.juzu.Response;
import org.juzu.View;
import org.juzu.template.Template;
import org.sample.Flash;
import org.sample.models.Hotel;
import org.sample.models.User;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 * @version $Id$
 *
 * Mar 12, 2012
 */
public class Application {

	@Inject @Path("index.gtmpl")
	Template index;
	
	@Inject @Path("register.gtmpl")
	Template register;
	
	@Inject
	Login login;
	
	@Inject
	Flash flash;
	
	@Inject
	Hotels hotels;
	
	@View
	public void index() {
		if(login.isConnected()) {
			hotels.index();
		} else {
			index.render();
		}
	}
	
	@View
	public void register() {
		register.render();
	}
	
	@Action
	public Response saveUser(String username, String name, String password, String verifyPassword) {
		User user = new User(name, password, verifyPassword);
		user.username = username;
		User.create(user);
		login.setUserName(user.username);
		flash.setSuccess("Welcome, " + user.name);
		return Application_.index();
	}
	
	@Action
	public Response login(String username, String password) {
		User user = User.find(username, password);
		if(user != null) {
			login.setUserName(user.username);
			flash.setSuccess("Welcome, " + user.name);
			return Hotels_.index();
		} else {
			flash.setUsername(username);
			flash.setError("Login failed");
			return null;
		}
	}
	
	@Action
	public Response logout() {
		login.setUserName(null);
		return Application_.index();
	}
}
