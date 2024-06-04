package com.axis.team2.technotribe.krasvbank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.axis.team2.technotribe.krasvbank.entity.Transaction;
import com.axis.team2.technotribe.krasvbank.repository.TransactionRepository;

import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @GetMapping("/{accountNumber}")
    public List<Transaction> getTransactionsByAccount(@PathVariable String accountNumber) {
        return transactionRepository.findByAccountNumber(accountNumber);
    }

    @PostMapping
    public Transaction createTransaction(@RequestBody Transaction transaction) {
        transaction.setTransactionDate(new Date(0));
        return transactionRepository.save(transaction);
    }
}

