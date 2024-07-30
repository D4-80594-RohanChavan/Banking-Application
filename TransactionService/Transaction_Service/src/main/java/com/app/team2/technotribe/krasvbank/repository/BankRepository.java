<<<<<<< HEAD
package com.app.team2.technotribe.krasvbank.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import com.app.team2.technotribe.krasvbank.entity.BankAccount;


public interface BankRepository extends JpaRepository<BankAccount,Long> {

	Boolean existsByEmail(String email);
	
	BankAccount findByAccountNumber(String accountNumber);

	boolean existsByAccountNumber(String accountNumber);
	
	Optional<BankAccount> findByEmail(String email);
	
	
}
=======
package com.app.team2.technotribe.krasvbank.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import com.app.team2.technotribe.krasvbank.entity.BankAccount;


public interface BankRepository extends JpaRepository<BankAccount,Long> {

	Boolean existsByEmail(String email);
	
	BankAccount findByAccountNumber(String accountNumber);

	boolean existsByAccountNumber(String accountNumber);
	
	Optional<BankAccount> findByEmail(String email);
	
	
}
>>>>>>> 1f121701983e37c5f071e9c7568ecab8f131e0af
