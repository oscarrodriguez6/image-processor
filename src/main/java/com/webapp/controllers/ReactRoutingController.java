package com.webapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ReactRoutingController {

	@RequestMapping(value = { "/viajes", "/viajes/", "/viajes/{path:^(?!index\\.html$).*$}" }, produces = "text/html")
	public String forwardReactRoutes() {
	    return "forward:/viajes/index.html";
	}
}