package com.app.team2.technotribe.krasvbank.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import com.app.team2.technotribe.krasvbank.entity.User;


public interface UserRepository extends JpaRepository<User,Long> {

	Boolean existsByEmail(String email);

	User findByAccountNumber(String accountNumber);

	boolean existsByAccountNumber(String accountNumber);
	
	Optional<User> findByEmail(String email);
	
}
