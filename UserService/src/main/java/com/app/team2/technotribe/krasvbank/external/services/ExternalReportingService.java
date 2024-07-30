package com.app.team2.technotribe.krasvbank.external.services;
import com.app.team2.technotribe.krasvbank.entity.Transaction;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.app.team2.technotribe.krasvbank.dto.MonthlyTransactionSummary;
import com.app.team2.technotribe.krasvbank.dto.TransactionReportDto;


@FeignClient(name = "ReportingService", url = "http://reportingservice:7078/reports")
//@FeignClient(name = "ReportingService", url = "http://localhost:7078/reports")
public interface ExternalReportingService {

	// fetch all credits, debits, transfers of last 12 months
	@GetMapping("/admin/monthly-summary")
	List<MonthlyTransactionSummary> getMonthlyTransactionSummary();
	
	// fetch all credits, debits, transfers of current months
	@GetMapping("/monthly-summary/current")
	public MonthlyTransactionSummary getCurrentMonthTransactionSummary();

	// fetch for single user all credits, debits, transfers of last 12 months
	@PostMapping("/admin/monthly-summary/user")
	List<MonthlyTransactionSummary> getMonthlyTransactionSummaryForUser(@RequestParam String accountNumber);

	// fetch for a single user all credits, debits, transfers of the current month
	@PostMapping("/admin/monthly-summary/user/current")
	public MonthlyTransactionSummary getCurrentMonthTransactionSummaryForUser(@RequestParam String accountNumber);
	
	@PostMapping("/transactions/all")
	List<Transaction> getAllTransactions(@RequestParam String accountNumber);
//---------------------------------------------------------------------------------------------------------------------------------------

	@GetMapping("/transactions/credits")
	TransactionReportDto getAllCredits(@RequestParam("accountNumber") String accountNumber);

	@GetMapping("/transactions/debits")
	TransactionReportDto getAllDebits(@RequestParam("accountNumber") String accountNumber);

	@GetMapping("/transactions/transfers")
	TransactionReportDto getAllFundTransfers(@RequestParam("accountNumber") String accountNumber);

	// current month transactions
	@GetMapping("/user/current-month-credits")
	TransactionReportDto getAllCreditsForCurrentMonth(@RequestParam("accountNumber") String accountNumber);

	@GetMapping("/user/current-month-debits")
	TransactionReportDto getAllDebitsForCurrentMonth(@RequestParam("accountNumber") String accountNumber);

	@GetMapping("/user/current-month-transfer")
	TransactionReportDto getAllTransferForCurrentMonth(@RequestParam("accountNumber") String accountNumber);

	// ADMIN
	@GetMapping("/admin/user-all-transactions")
	TransactionReportDto getAllTransactionsByUser(@RequestParam("accountNumber") String accountNumber);

	@GetMapping("/admin/user-credits")
	TransactionReportDto getAllCreditsByUser(@RequestParam("accountNumber") String accountNumber);

	@GetMapping("/admin/user-debits")
	TransactionReportDto getAllDebitsByUser(@RequestParam("accountNumber") String accountNumber);

	@GetMapping("/admin/user-transfers")
	TransactionReportDto getAllFundTransfersByUser(@RequestParam("accountNumber") String accountNumber);

}
