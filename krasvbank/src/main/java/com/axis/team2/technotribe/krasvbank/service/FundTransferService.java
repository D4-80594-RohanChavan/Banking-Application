package com.axis.team2.technotribe.krasvbank.service;


import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.axis.team2.technotribe.krasvbank.dto.FundTransferRequest;
import com.axis.team2.technotribe.krasvbank.entity.Account;
import com.axis.team2.technotribe.krasvbank.entity.Transaction;

@Service
public class FundTransferService {

    @Autowired
    private RestTemplate restTemplate;

    private final String ACCOUNT_SERVICE_URL = "http://localhost:8085/accounts";
    private final String TRANSACTION_SERVICE_URL = "http://localhost:8085/transactions";

    public String transferFunds(FundTransferRequest request) {
        try {
            Account fromAccount = restTemplate.getForObject(ACCOUNT_SERVICE_URL + "/" + request.getFromAccount(), Account.class);
            Account toAccount = restTemplate.getForObject(ACCOUNT_SERVICE_URL + "/" + request.getToAccount(), Account.class);

            if (fromAccount.getBalance() >= request.getAmount()) {
                fromAccount.setBalance(fromAccount.getBalance() - request.getAmount());
                toAccount.setBalance(toAccount.getBalance() + request.getAmount());

                restTemplate.put(ACCOUNT_SERVICE_URL + "/" + fromAccount.getId(), fromAccount);
                restTemplate.put(ACCOUNT_SERVICE_URL + "/" + toAccount.getId(), toAccount);

                Transaction debitTransaction = new Transaction();
                debitTransaction.setAccountNumber(request.getFromAccount());
                debitTransaction.setAmount(request.getAmount());
                debitTransaction.setTransactionType("DEBIT");

                Transaction creditTransaction = new Transaction();
                creditTransaction.setAccountNumber(request.getToAccount());
                creditTransaction.setAmount(request.getAmount());
                creditTransaction.setTransactionType("CREDIT");

                restTemplate.postForObject(TRANSACTION_SERVICE_URL, debitTransaction, Transaction.class);
                restTemplate.postForObject(TRANSACTION_SERVICE_URL, creditTransaction, Transaction.class);

                return "Funds transferred successfully!";
            } else {
                return "Insufficient balance!";
            }
        } catch (HttpClientErrorException e) {
            // Handle HTTP errors (e.g., 404, 400)
            return "Error during fund transfer: " + e.getMessage();
        } catch (NoSuchElementException e) {
            return "Error during fund transfer: Account not found";
        }
    }
}

