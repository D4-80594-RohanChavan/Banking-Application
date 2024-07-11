package com.axis.team2.technotribe.krasvbank.repository;

import java.time.LocalDateTime;
import java.util.List;

//UserRepository.java

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.axis.team2.technotribe.krasvbank.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
 User findByAccountNumber(String accountNumber);
 
//Method to find users by creation timestamp between two dates
 List<User> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

 List<User> findByStatus(String status);
 // Method to fetch count
 @Query("SELECT COUNT(u) FROM User u WHERE u.status = :status")
 long countByStatus(@Param("status") String status);
}

