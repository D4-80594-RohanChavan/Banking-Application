package com.app.team2.technotribe.krasvbank.service.impl;

import java.math.BigDecimal;

import com.app.team2.technotribe.krasvbank.dto.BankResponse;
import com.app.team2.technotribe.krasvbank.dto.CreditDebitRequest;
import com.app.team2.technotribe.krasvbank.dto.EnquiryRequest;
import com.app.team2.technotribe.krasvbank.dto.TransactionDto;
import com.app.team2.technotribe.krasvbank.dto.TransferRequest;

public interface UserService {

	

//	BankResponse balanceEnquiry(EnquiryRequest enq);
	BigDecimal balanceEnquiry(EnquiryRequest enq);

	String nameEnquiry(EnquiryRequest request);

	BankResponse creditAccount(CreditDebitRequest request);

	BankResponse debitAccount(CreditDebitRequest request);

	BankResponse transfer(TransferRequest request);
	
}
 