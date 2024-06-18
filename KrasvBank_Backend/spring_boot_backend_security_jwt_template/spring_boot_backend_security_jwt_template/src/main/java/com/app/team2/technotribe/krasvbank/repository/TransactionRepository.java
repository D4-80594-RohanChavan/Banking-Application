package com.app.team2.technotribe.krasvbank.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.team2.technotribe.krasvbank.entity.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,String>{

	
}
