package com.app.team2.technotribe.krasvbank.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.team2.technotribe.krasvbank.dto.TransactionDto;
import com.app.team2.technotribe.krasvbank.entity.Transaction;
import com.app.team2.technotribe.krasvbank.repository.TransactionRepository;

@Service
public class TransactionServiceImpl implements TransactionService {

	@Autowired
	TransactionRepository transactionRepository;

	@Override
	public void saveTransaction(TransactionDto transactionDto) {
		 Transaction transaction = new Transaction();
	        BeanUtils.copyProperties(transactionDto, transaction);
	        transactionRepository.save(transaction);
//		Transaction transaction=Transaction.builder()
//				.transactionType(transactionDto.getTransactionType())
//				.accountNumber(transactionDto.getAccountNumber())
//				.amount(transactionDto.getAmount())
//				.status("SUCCESS")
//				.build();
//		transactionRepository.save(transaction);
		System.out.println("Transaction saved successfully");

	}

}
