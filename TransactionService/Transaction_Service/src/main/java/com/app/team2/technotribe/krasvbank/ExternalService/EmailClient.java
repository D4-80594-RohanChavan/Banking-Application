package com.app.team2.technotribe.krasvbank.ExternalService;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.app.team2.technotribe.krasvbank.dto.EmailDetailsDto;

@FeignClient(name = "emailClient", url = "http://notificationservice:7082/api/notification")
//@FeignClient(name = "emailClient", url = "http://localhost:7082/api/notification")
public interface EmailClient {

	@PostMapping("/sendEmail")
	void sendEmailAlert(@RequestBody EmailDetailsDto email);
}
