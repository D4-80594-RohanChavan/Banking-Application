package com.app.team2.technotribe.krasvbank.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.team2.technotribe.krasvbank.dto.EmailDetailsDto;
import com.app.team2.technotribe.krasvbank.service.impl.EmailService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/notification")
@Tag(name = "User Account Management APIs")
public class NotificationController {

	@Autowired
	EmailService emailService;

	@PostMapping("sendEmail")
	public void SendEmail(@RequestBody EmailDetailsDto email) {

		emailService.sendEmailAlert(email);
	}
	@PostMapping("sendEmailwithAttachment")
	public void SendEmailWithAttachment(@RequestBody EmailDetailsDto email) {

		emailService.sendEmailWithAttachment(email);
	}

	
}
