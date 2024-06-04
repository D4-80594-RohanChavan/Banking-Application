package com.axis.team2.technotribe.krasvbank.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.axis.team2.technotribe.krasvbank.entity.Transaction;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccountNumber(String accountNumber);
}


