package com.axis.team2.technotribe.krasvbank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.axis.team2.technotribe.krasvbank.dto.MonthlyTransactionSummary;
import com.axis.team2.technotribe.krasvbank.dto.TransactionReport;
import com.axis.team2.technotribe.krasvbank.entity.Transaction;
import com.axis.team2.technotribe.krasvbank.service.ReportingService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reports")
public class ReportingController {

	@Autowired
	private ReportingService reportingService;

	
	@GetMapping("")
	public String getstatus() {
		return "inside reporting controller";
	}
	// -----------------------------------------------------------------------
	// USER

	// fetch all credits, debits, transfers of last 12 months
	@GetMapping("/admin/monthly-summary")
	public List<MonthlyTransactionSummary> getMonthlyTransactionSummary() {
		return reportingService.getMonthlyTransactionSummary();
	}

	// fetch all credits, debits, transfers of current month
	@GetMapping("/monthly-summary/current")
	public MonthlyTransactionSummary getCurrentMonthTransactionSummary() {
		return reportingService.getCurrentMonthTransactionSummary();
	}

	// fetch for single user all credits, debits, transfers of last 12 months
	@PostMapping("/admin/monthly-summary/user")
	public List<MonthlyTransactionSummary> getMonthlyTransactionSummaryForUser(@RequestParam String accountNumber) {
		return reportingService.getMonthlyTransactionSummaryForUser(accountNumber);
	}

	// fetch for a single user all credits, debits, transfers of the current month
	@PostMapping("/admin/monthly-summary/user/current")
	public MonthlyTransactionSummary getCurrentMonthTransactionSummaryForUser(@RequestParam String accountNumber) {
		return reportingService.getCurrentMonthTransactionSummaryForUser(accountNumber);
	}

	@PostMapping("/transactions/all")
	public List<Transaction> getAllTransactions(@RequestParam String accountNumber) {
		return reportingService.getAllTransactions(accountNumber);
	}
//---------------------------------------------------------------------------------------------------------------------------------------

	// fetch all active users count
	@GetMapping("/admin/active-users-count")
	public long getCountOfActiveUsers() {
		return reportingService.getCountOfActiveUsers();
	}

	// fetch all Inactive users count
	@GetMapping("/admin/inactive-users-count")
	public long getCountOfInactiveUsers() {
		return reportingService.getCountOfInactiveUsers();
	}

	// fetch all the transactions by user
// @GetMapping("/transactions/all")
// public TransactionReport getAllTransactions(@RequestParam String accountNumber) {
//     return reportingService.getAllTransactions(accountNumber);
// }

	// fetch all the credits by user
	@GetMapping("/transactions/credits")
	public TransactionReport getAllCredits(@RequestParam String accountNumber) {
		return reportingService.getAllCredits(accountNumber);
	}

	// fetch all the debits by user
	@GetMapping("/transactions/debits")
	public TransactionReport getAllDebits(@RequestParam String accountNumber) {
		return reportingService.getAllDebits(accountNumber);
	}

	// fetch all the transfers by user
	@GetMapping("/transactions/transfers")
	public TransactionReport getAllFundTransfers(@RequestParam String accountNumber) {
		return reportingService.getAllFundTransfers(accountNumber);
	}

	// fetch all credits for current month
	@GetMapping("/user/current-month-credits")
	public TransactionReport getAllCreditsForCurrentMonth(@RequestParam String accountNumber) {
		return reportingService.getAllCreditsForCurrentMonth(accountNumber);
	}

	// fetch all debits for current month
	@GetMapping("/user/current-month-debits")
	public TransactionReport getAllDebitsForCurrentMonth(@RequestParam String accountNumber) {
		return reportingService.getAllDebitsForCurrentMonth(accountNumber);
	}

	// ADMIN

	// fetch all transactions within a specified timeframe for all users
// @GetMapping("/admin/transactions")
// public TransactionReport getAllTransactionsWithinTimeframe(
//         @RequestParam String startDate,
//         @RequestParam String endDate) {
//
//     LocalDateTime start = LocalDateTime.parse(startDate);
//     LocalDateTime end = LocalDateTime.parse(endDate);
//     return reportingService.getAllTransactionsWithinTimeframe(start, end);
// }

	// fetch all transactions within a specified timeframe for a specific user
// @GetMapping("/admin/user-transactions")
// public TransactionReport getTransactionsByUserWithinTimeframe(
//         @RequestParam String accountNumber,
//         @RequestParam String startDate,
//         @RequestParam String endDate) {
//
//     LocalDateTime start = LocalDateTime.parse(startDate);
//     LocalDateTime end = LocalDateTime.parse(endDate);
//     return reportingService.getTransactionsByUserWithinTimeframe(accountNumber, start, end);
// }

	// fetch all transactions for a specific user
	@GetMapping("/admin/user-all-transactions")
	public TransactionReport getAllTransactionsByUser(@RequestParam String accountNumber) {
		return reportingService.getAllTransactionsByUser(accountNumber);
	}

	// fetch all credits for a specific user
	@GetMapping("/admin/user-credits")
	public TransactionReport getAllCreditsByUser(@RequestParam String accountNumber) {
		return reportingService.getAllCreditsByUser(accountNumber);
	}

	// fetch all debits for a specific user
	@GetMapping("/admin/user-debits")
	public TransactionReport getAllDebitsByUser(@RequestParam String accountNumber) {
		return reportingService.getAllDebitsByUser(accountNumber);
	}

	// fetch all fund transfers for a specific user
	@GetMapping("/admin/user-transfers")
	public TransactionReport getAllFundTransfersByUser(@RequestParam String accountNumber) {
		return reportingService.getAllFundTransfersByUser(accountNumber);
	}

	// fetch all users created within a time-frame
// @GetMapping("/admin/users-created")
// public List<User> getUsersCreatedWithinTimeframe(
//         @RequestParam String startDate,
//         @RequestParam String endDate) {
//
//     LocalDateTime start = LocalDateTime.parse(startDate);
//     LocalDateTime end = LocalDateTime.parse(endDate);
//     return reportingService.getUsersCreatedWithinTimeframe(start, end);
// }
	
	
	//------------------------------------------------------------------------------------------------------------
	// Create a new transaction
    @PostMapping("/transactions")
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
        Transaction savedTransaction = reportingService.saveTransaction(transaction);
        return ResponseEntity.ok(savedTransaction);
    }

    // Retrieve a transaction by its ID
    @GetMapping("/transactions/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable String id) {
        Optional<Transaction> transaction = reportingService.getTransactionById(id);
        return transaction.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Update an existing transaction
    @PutMapping("/transactions/{id}")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable String id, @RequestBody Transaction transactionDetails) {
        Transaction updatedTransaction = reportingService.updateTransaction(id, transactionDetails);
        return ResponseEntity.ok(updatedTransaction);
    }

    // Delete a transaction
    @DeleteMapping("/transactions/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable String id) {
    	reportingService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }

}
