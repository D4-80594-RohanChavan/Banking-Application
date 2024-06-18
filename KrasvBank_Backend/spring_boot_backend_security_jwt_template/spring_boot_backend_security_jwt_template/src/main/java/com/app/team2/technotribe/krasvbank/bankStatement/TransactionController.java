package com.app.team2.technotribe.krasvbank.bankStatement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.app.team2.technotribe.krasvbank.entity.Transaction;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/bankStatement")
@AllArgsConstructor
public class TransactionController {

	@Autowired
	private BankStatement bankStatement;

	@GetMapping
	public List<Transaction> generateBankStatemant(@RequestParam String accountNumber, @RequestParam String startDate,
			@RequestParam String endDate) {
		return bankStatement.generateStatement(accountNumber, startDate, endDate);
	}
}
