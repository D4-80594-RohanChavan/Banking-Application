package com.app.team2.technotribe.krasvbank.service.impl;

import org.springframework.stereotype.Service;

import com.app.team2.technotribe.krasvbank.dto.TransactionDto;
import com.app.team2.technotribe.krasvbank.entity.Transaction;

@Service
public interface TransactionService {

	void saveTransaction(TransactionDto transactionDto);
	
}
