package com.app.team2.technotribe.krasvbank.service.impl;

import org.springframework.stereotype.Service;

import com.app.team2.technotribe.krasvbank.dto.EmailDetailsDto;

@Service
public interface EmailService {
	void sendEmailAlert(EmailDetailsDto emailDetails);
	void sendEmailWithAttachment(EmailDetailsDto emailDetails);
}
