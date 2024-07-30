package com.app.team2.technotribe.krasvbank.service.impl;

import org.springframework.stereotype.Service;

import com.app.team2.technotribe.krasvbank.dto.EmailDetails;

@Service
public interface EmailService {
	void sendEmailAlert(EmailDetails emailDetails);
	void sendEmailWithAttachment(EmailDetails emailDetails);
}
