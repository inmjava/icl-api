package com.copel.picmicroservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MonitorController {
	//@RequestMapping("/monitor")
	public String monitor() {
		return "OK OK OK OK " + System.currentTimeMillis();
	}
}