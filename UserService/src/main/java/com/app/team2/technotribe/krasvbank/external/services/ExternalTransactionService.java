package com.app.team2.technotribe.krasvbank.external.services;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.team2.technotribe.krasvbank.dto.BankResponse;
import com.app.team2.technotribe.krasvbank.dto.CreateBankAccountDto;
import com.app.team2.technotribe.krasvbank.dto.CreditDebitRequest;
import com.app.team2.technotribe.krasvbank.dto.TransferRequest;
import com.app.team2.technotribe.krasvbank.entity.Transaction;

@FeignClient(name = "TransactionService", url = "http://transactionservice:7074/api/user")
//@FeignClient(name = "TransactionService", url = "http://localhost:7074/api/user")
public interface ExternalTransactionService {

	@GetMapping("/balanceEnquiry/{accountNumber}")
	BigDecimal balanceEnquiry(@PathVariable("accountNumber") String accountNumber);

	@PostMapping("/credit")
	BankResponse creditAccount(@RequestBody CreditDebitRequest request);

	@PostMapping("/debit")
	BankResponse debitAccount(@RequestBody CreditDebitRequest request);

	 @PostMapping("/transfer")
	    BankResponse transfer(@RequestBody TransferRequest request);

	 @GetMapping("/bankStatement")
	    List<Transaction> generateBankStatement(
	            @RequestParam("accountNumber") String accountNumber,
	            @RequestParam("startDate") String startDate,
	            @RequestParam("endDate") String endDate);
	 
	 @PostMapping("/createaccount")
	    String createAccount(@RequestBody CreateBankAccountDto newAccount);
}
