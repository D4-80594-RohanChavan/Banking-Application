<<<<<<< HEAD
package com.app.team2.technotribe.krasvbank.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.team2.technotribe.krasvbank.bankStatementPdfGenerator.BankStatement;
import com.app.team2.technotribe.krasvbank.dto.BankResponse;
import com.app.team2.technotribe.krasvbank.dto.CreditDebitRequest;
import com.app.team2.technotribe.krasvbank.dto.EnquiryRequest;
import com.app.team2.technotribe.krasvbank.dto.TransferRequest;
import com.app.team2.technotribe.krasvbank.entity.BankAccount;
import com.app.team2.technotribe.krasvbank.entity.Transaction;
import com.app.team2.technotribe.krasvbank.service.impl.BankService;
import com.app.team2.technotribe.krasvbank.repository.BankRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/user")
@Tag(name = "User Account Management APIs")
public class BankController {

	@Autowired
	BankService bankService;
	@Autowired
	BankRepository userRepository;

	@Autowired
	private BankStatement bankStatement;

	@PostMapping("createaccount")
	public String createBankAccount(@RequestBody BankAccount newAccount) {

		return bankService.createAccount(newAccount);
	}

	@GetMapping("bankStatement")
	public List<Transaction> generateBankStatemant(@RequestParam String accountNumber, @RequestParam String startDate,
			@RequestParam String endDate) {
		return bankStatement.generateStatement(accountNumber, startDate, endDate);
	}

	@Operation(summary = "Balance Enquiry", description = "Given an account number, cheak how much the user has")
	@ApiResponse(responseCode = "201", description = "Http Status 201 SUCCESS")
	@GetMapping("/balanceEnquiry/{accountNumber}")
	public BigDecimal balanceEnquiry(@PathVariable("accountNumber") String accountNumber) {
		System.out.println("inside transactionservice controller");
		EnquiryRequest enquiryRequest = new EnquiryRequest(accountNumber);
		return bankService.balanceEnquiry(enquiryRequest);
	}

	@PostMapping("credit")
	public BankResponse creditAccount(@RequestBody CreditDebitRequest request) {
		return bankService.creditAccount(request);
	}

	@PostMapping("debit")
	public BankResponse debitAccount(@RequestBody CreditDebitRequest request) {
		return bankService.debitAccount(request);
	}

	@PostMapping("transfer")
	public BankResponse transfer(@RequestBody TransferRequest request) {
		return bankService.transfer(request);
	}

	@GetMapping("nameEnquiry")
	public String nameEnquiry(@RequestBody EnquiryRequest request) {
		return bankService.nameEnquiry(request);
	}

}
=======
package com.app.team2.technotribe.krasvbank.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.team2.technotribe.krasvbank.bankStatementPdfGenerator.BankStatement;
import com.app.team2.technotribe.krasvbank.dto.BankResponse;
import com.app.team2.technotribe.krasvbank.dto.CreditDebitRequest;
import com.app.team2.technotribe.krasvbank.dto.EnquiryRequest;
import com.app.team2.technotribe.krasvbank.dto.TransferRequest;
import com.app.team2.technotribe.krasvbank.entity.BankAccount;
import com.app.team2.technotribe.krasvbank.entity.Transaction;
import com.app.team2.technotribe.krasvbank.service.impl.BankService;
import com.app.team2.technotribe.krasvbank.repository.BankRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/user")
@Tag(name = "User Account Management APIs")
public class BankController {

	@Autowired
	BankService bankService;
	@Autowired
	BankRepository userRepository;

	@Autowired
	private BankStatement bankStatement;

	@PostMapping("createaccount")
	public String createBankAccount(@RequestBody BankAccount newAccount) {

		return bankService.createAccount(newAccount);
	}

	@GetMapping("bankStatement")
	public List<Transaction> generateBankStatemant(@RequestParam String accountNumber, @RequestParam String startDate,
			@RequestParam String endDate) {
		return bankStatement.generateStatement(accountNumber, startDate, endDate);
	}

	@Operation(summary = "Balance Enquiry", description = "Given an account number, cheak how much the user has")
	@ApiResponse(responseCode = "201", description = "Http Status 201 SUCCESS")
	@GetMapping("/balanceEnquiry/{accountNumber}")
	public BigDecimal balanceEnquiry(@PathVariable("accountNumber") String accountNumber) {
		System.out.println("inside transactionservice controller");
		EnquiryRequest enquiryRequest = new EnquiryRequest(accountNumber);
		return bankService.balanceEnquiry(enquiryRequest);
	}

	@PostMapping("credit")
	public BankResponse creditAccount(@RequestBody CreditDebitRequest request) {
		return bankService.creditAccount(request);
	}

	@PostMapping("debit")
	public BankResponse debitAccount(@RequestBody CreditDebitRequest request) {
		return bankService.debitAccount(request);
	}

	@PostMapping("transfer")
	public BankResponse transfer(@RequestBody TransferRequest request) {
		return bankService.transfer(request);
	}

	@GetMapping("nameEnquiry")
	public String nameEnquiry(@RequestBody EnquiryRequest request) {
		return bankService.nameEnquiry(request);
	}

}
>>>>>>> 1f121701983e37c5f071e9c7568ecab8f131e0af
