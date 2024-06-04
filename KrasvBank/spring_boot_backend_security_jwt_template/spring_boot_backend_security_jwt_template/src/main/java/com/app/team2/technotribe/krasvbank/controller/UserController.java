package com.app.team2.technotribe.krasvbank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.app.team2.technotribe.krasvbank.dto.BankResponse;
import com.app.team2.technotribe.krasvbank.dto.CreditDebitRequest;
import com.app.team2.technotribe.krasvbank.dto.EnquiryRequest;
import com.app.team2.technotribe.krasvbank.dto.UserRequest;
import com.app.team2.technotribe.krasvbank.service.impl.UserService;


@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired 
	UserService userService;
	
	@PostMapping
	public BankResponse createAccount(@RequestBody UserRequest userRequest) {
		return userService.createAccount(userRequest);
	}
	
	@GetMapping("/balanceEnquiry")
	public BankResponse balanceEnquiry(@RequestBody EnquiryRequest request) {
		return userService.balanceEnquiry(request);
	}
	
	@GetMapping("nameEnquiry")
	public String nameEnquiry(@RequestBody EnquiryRequest request) {
		return userService.nameEnquiry(request);
	}
	
	@PostMapping("credit")
	public BankResponse creditAccount(@RequestBody CreditDebitRequest request) {
		return userService.creditAccount(request);
	}
	
	@PostMapping("debit")
	public BankResponse debitAccount(@RequestBody CreditDebitRequest request) {
		return userService.debitAccount(request);
	}
	
}
