package com.app.team2.technotribe.krasvbank.bankStatement;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.team2.technotribe.krasvbank.entity.Transaction;
import com.app.team2.technotribe.krasvbank.external.services.ExternalTransactionService;
import com.app.team2.technotribe.krasvbank.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BankStatement {
	
	@Autowired
	ExternalTransactionService externalTransactionService;
	
//	@Autowired
//	TransactionRepository transactionRepository;
	@Autowired
	UserRepository userRepository;
	
	
	public List<Transaction> generateStatement(String accountNumber, String startDate, String endDate) {
		
		return externalTransactionService.generateBankStatement(accountNumber, startDate, endDate);

	}

}
