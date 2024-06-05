package com.app.team2.technotribe.krasvbank.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.app.team2.technotribe.krasvbank.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction,String>{

}
