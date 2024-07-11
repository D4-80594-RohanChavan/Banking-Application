package com.axis.team2.technotribe.krasvbank.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.axis.team2.technotribe.krasvbank.entity.Transaction;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, String> {
	//USER
 List<Transaction> findByAccountNumberAndCreatedAtBetween(String accountNumber, LocalDateTime startDate, LocalDateTime endDate);
 List<Transaction> findByAccountNumber(String accountNumber);
 List<Transaction> findByAccountNumberAndTransactionType(String accountNumber, String transactionType);
 List<Transaction> findByAccountNumberAndTransactionTypeAndCreatedAtBetween(String accountNumber, String transactionType, LocalDateTime startDate, LocalDateTime endDate);
 List<Transaction> findByTransactionTypeAndCreatedAtBetween(String transactionType, LocalDateTime startDate, LocalDateTime endDate);
 //ADMIN
 List<Transaction> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
}

