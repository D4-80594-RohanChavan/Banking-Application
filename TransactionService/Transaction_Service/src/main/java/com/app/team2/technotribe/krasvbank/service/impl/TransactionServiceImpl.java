<<<<<<< HEAD
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
		System.out.println("Transaction saved successfully");

	}

}
=======
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
		System.out.println("Transaction saved successfully");

	}

}
>>>>>>> 1f121701983e37c5f071e9c7568ecab8f131e0af
