package com.app.team2.technotribe.krasvbank.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import com.app.team2.technotribe.krasvbank.entity.BankAccount;


public interface UserRepository extends JpaRepository<BankAccount,Long> {

	Boolean existsByEmail(String email);

	BankAccount findByAccountNumber(String accountNumber);

	boolean existsByAccountNumber(String accountNumber);
	
	Optional<BankAccount> findByEmail(String email);
	
	
}
