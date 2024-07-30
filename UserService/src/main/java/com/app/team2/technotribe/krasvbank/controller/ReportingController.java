package com.app.team2.technotribe.krasvbank.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.team2.technotribe.krasvbank.dto.TransactionReportDto;
import com.app.team2.technotribe.krasvbank.external.services.ExternalReportingService;
import com.app.team2.technotribe.krasvbank.entity.Transaction;
import com.app.team2.technotribe.krasvbank.dto.MonthlyTransactionSummary;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin("*")
public class ReportingController {

	@Autowired
	ExternalReportingService externalReportingService;

	// fetch all credits, debits, transfers of last 12 months
	@GetMapping("/admin/monthly-summary")
	public List<MonthlyTransactionSummary> getMonthlyTransactionSummary() {
		return externalReportingService.getMonthlyTransactionSummary();
	}

	// fetch all credits, debits, transfers of current months
	@GetMapping("/admin/monthly-summary/current")
	public MonthlyTransactionSummary getCurrentMonthTransactionSummary() {
		return externalReportingService.getCurrentMonthTransactionSummary();
	}

	// fetch for single user all credits, debits, transfers of last 12 months
	@PostMapping("/monthly-summary/user")
	public List<MonthlyTransactionSummary> getMonthlyTransactionSummaryForUser(@RequestParam String accountNumber) {
		return externalReportingService.getMonthlyTransactionSummaryForUser(accountNumber);
	}

	// fetch for a single user all credits, debits, transfers of the current month
	@PostMapping("/monthly-summary/user/current")
	public MonthlyTransactionSummary getCurrentMonthTransactionSummaryForUser(@RequestParam String accountNumber) {
		return externalReportingService.getCurrentMonthTransactionSummaryForUser(accountNumber);
	}

	@PostMapping("/transactions/all")
	public List<Transaction> getAllTransactions(@RequestParam String accountNumber) {
		return externalReportingService.getAllTransactions(accountNumber);
	}


	@GetMapping("/transactions/credits")
	public TransactionReportDto getAllCredits(@RequestParam String accountNumber) {
		return externalReportingService.getAllCredits(accountNumber);
	}

	@GetMapping("/transactions/debits")
	public TransactionReportDto getAllDebits(@RequestParam String accountNumber) {
		return externalReportingService.getAllDebits(accountNumber);
	}

	@GetMapping("/transactions/transfers")
	public TransactionReportDto getAllFundTransfers(@RequestParam String accountNumber) {
		return externalReportingService.getAllFundTransfers(accountNumber);
	}

	@GetMapping("/user/current-month-credits")
	public TransactionReportDto getAllCreditsForCurrentMonth(@RequestParam String accountNumber) {
		return externalReportingService.getAllCreditsForCurrentMonth(accountNumber);
	}

	@GetMapping("/user/current-month-debits")
	public TransactionReportDto getAllDebitsForCurrentMonth(@RequestParam String accountNumber) {
		return externalReportingService.getAllDebitsForCurrentMonth(accountNumber);
	}

	@GetMapping("/user/current-month-transfer")
	public TransactionReportDto getAllTransferForCurrentMonth(@RequestParam String accountNumber) {
		return externalReportingService.getAllTransferForCurrentMonth(accountNumber);
	}

	// ADMIN

	@GetMapping("/admin/user-all-transactions")
	public TransactionReportDto getAllTransactionsByUser(@RequestParam String accountNumber) {
		return externalReportingService.getAllTransactionsByUser(accountNumber);
	}

	@GetMapping("/admin/user-debits")
	public TransactionReportDto getAllDebitsByUser(@RequestParam String accountNumber) {
		return externalReportingService.getAllDebitsByUser(accountNumber);
	}

	@GetMapping("/admin/user-credits")
	public TransactionReportDto getAllCreditsByUser(@RequestParam String accountNumber) {
		return externalReportingService.getAllCreditsByUser(accountNumber);
	}

	@GetMapping("/admin/user-transfers")
	public TransactionReportDto getAllFundTransfersByUser(@RequestParam String accountNumber) {
		return externalReportingService.getAllFundTransfersByUser(accountNumber);
	}

}
