package com.axis.team2.technotribe.krasvbank.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.axis.team2.technotribe.krasvbank.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByAccountNumber(String accountNumber);
}

