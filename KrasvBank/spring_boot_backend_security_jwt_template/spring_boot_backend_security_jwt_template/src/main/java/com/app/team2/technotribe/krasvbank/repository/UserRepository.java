package com.app.team2.technotribe.krasvbank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.team2.technotribe.krasvbank.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

	Boolean existsByEmail(String email);

	User findByAccountNumber(String accountNumber);

	boolean existsByAccountNumber(String accountNumber);
	
}
