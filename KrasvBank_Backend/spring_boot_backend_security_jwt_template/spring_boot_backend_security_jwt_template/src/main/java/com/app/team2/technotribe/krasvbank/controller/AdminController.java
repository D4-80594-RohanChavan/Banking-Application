package com.app.team2.technotribe.krasvbank.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

	@GetMapping
	public String admin() {
		return "admin page";
	}
}
