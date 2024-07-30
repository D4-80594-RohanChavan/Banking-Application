package com.app.team2.technotribe.krasvbank.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.team2.technotribe.krasvbank.entity.User;


public interface UserRepository extends JpaRepository<User,Long> {

	Boolean existsByEmail(String email);

	User findByAccountNumber(String accountNumber);

	boolean existsByAccountNumber(String accountNumber);
	
	Optional<User> findByEmail(String email);
	
	List<User> findByStatus(String status);
	
//	// Method to fetch count
//	 @Query("SELECT COUNT(u) FROM User u WHERE u.status = :status")
//	 long countByStatus(@Param("status") String status);
	 
	 int countByStatus(String status);
	
}
