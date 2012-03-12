@org.juzu.Application(
	defaultController = org.sample.controllers.Application.class,
	plugins = {
		AssetPlugin.class,
		AjaxPlugin.class
	}
)

@Assets(
	scripts = {
		@Script(src = "public/javascripts/jquery-1.7.1.min.js"),
      @Script(src = "public/javascripts/jquery-ui-1.7.2.custom.min.js"),
      @Script(src = "public/javascripts/booking.js")
	},
	stylesheets = {
		@Stylesheet(src = "public/stylesheets/main.css")
	}
)
package org.sample;

import org.juzu.plugin.ajax.*;
import org.juzu.plugin.asset.*;

