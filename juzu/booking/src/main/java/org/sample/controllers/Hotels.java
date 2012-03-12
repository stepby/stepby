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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.inject.Inject;

import org.juzu.Action;
import org.juzu.Path;
import org.juzu.Resource;
import org.juzu.Response;
import org.juzu.View;
import org.juzu.plugin.ajax.Ajax;
import org.juzu.template.Template;
import org.sample.Flash;
import org.sample.models.Booking;
import org.sample.models.Hotel;
import org.sample.models.User;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 * @version $Id$
 * 
 *          Mar 12, 2012
 */
public class Hotels {

	@Inject
	Login login;

	@Inject
	Flash flash;

	@Inject
	@Path("hotels/index.gtmpl")
	org.sample.templates.hotels.index index;

	@Inject
	@Path("hotels/list.gtmpl")
	org.sample.templates.hotels.list list;

	@Inject
	@Path("hotels/book.gtmpl")
	org.sample.templates.hotels.book book;

	@Inject
	@Path("hotels/show.gtmpl")
	org.sample.templates.hotels.show show;
	
	@Inject
	@Path("hotels/confirmBooking.gtmpl")
	Template confirmBooking;

	@View
	public void index() {
		String username = login.getUserName();
		List<Booking> bookings = Booking.findByUser(username);
		index.with().bookings(bookings).render();
	}

	@View
	public void settings() {
		throw new UnsupportedOperationException("todo settings.gtmpl");
	}

	@View
	public void show(String id) {
      Hotel hotel = Hotel.findById(id);
      show.with().hotel(hotel).render();
	}

	@View
	public void book(String id) {
		Hotel hotel = Hotel.findById(id);
      book.with().hotel(hotel).render();
	}

	@Ajax
	@Resource
	public void list(String search, String size, String page) {
		int _size = size != null ? Integer.parseInt(size) : 5;
		int _page = page != null ? Integer.parseInt(page) : 0;
		List<Hotel> hotels;
		Pattern pattern;
		if (search != null && search.trim().length() > 0) {
			pattern = Pattern.compile(".*" + Pattern.quote(search.trim()) + ".*", Pattern.CASE_INSENSITIVE);
		}
		else {
			pattern = Pattern.compile(".*");
		}
		hotels = Hotel.find(pattern, pattern, _size, _page);
		list.with().hotels(hotels).page(_page).render();
	}

	@Action
	public Response cancelBooking(String id) {
		Booking booking = Booking.find(id);
		booking.delete();
		flash.setSuccess("Booking cancel for booking number " + id);
		return Hotels_.index();
	}

	@Action
	public Response processConfirmBooking(String confirm, String id, String checkinDate, String checkoutDate,
		String beds, String smoking, String creditCard, String creditCardName, String creditCardExpiryMonth,
		String creditCardExpiryYear) {
		
		Hotel hotel = Hotel.findById(id);
      User user = User.find(login.getUserName(), null);
      Booking booking = new Booking(hotel, user);
      booking.checkinDate = checkinDate;
      booking.checkoutDate = checkoutDate;
      booking.beds = Integer.parseInt(beds);
      booking.smoking = Boolean.valueOf(smoking);
      booking.creditCard = creditCard;
      booking.creditCardName = creditCardName;
      booking.creditCardExpiryMonth = Integer.parseInt(creditCardExpiryMonth);
      booking.creditCardExpiryYear = Integer.parseInt(creditCardExpiryYear);
      
      if (confirm != null)
      {
         booking.create();
         flash.setSuccess("Thank you, " + login.getUserName() + ", your confimation number for " + hotel.name
            + " is " + booking.id);
         return Hotels_.index();
      }
      else
      {
      	return Hotels_.confirmBooking(id, checkinDate, checkoutDate, beds, smoking, creditCard, creditCardName, creditCardExpiryMonth, creditCardExpiryYear);
      }
	}
	
	@View
	public void confirmBooking(String id, String checkinDate, String checkoutDate, String beds, String smoking,
		String creditCard, String creditCardName, String creditCardExpiryMonth, String creditCardExpiryYear) {
		
		 Map<String, Object> context = new HashMap<String, Object>();
	      Hotel hotel = Hotel.findById(id);
	      context.put("total", 0);
	      context.put("hotel", hotel);
	      context.put("checkinDate", checkinDate);
	      context.put("checkoutDate", checkoutDate);
	      context.put("beds", beds);
	      context.put("smoking", smoking);
	      context.put("creditCard", creditCard);
	      context.put("creditCardName", creditCardName);
	      context.put("creditCardExpiryMonth", creditCardExpiryMonth);
	      context.put("creditCardExpiryYear", creditCardExpiryYear);
	      confirmBooking.render(context);
	}
}
