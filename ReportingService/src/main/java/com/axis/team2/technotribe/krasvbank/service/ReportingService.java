package com.axis.team2.technotribe.krasvbank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.axis.team2.technotribe.krasvbank.ExceptionHandling.ResourceNotFoundException;
import com.axis.team2.technotribe.krasvbank.dto.MonthlyTransactionSummary;
import com.axis.team2.technotribe.krasvbank.dto.TransactionReport;
import com.axis.team2.technotribe.krasvbank.entity.Transaction;
import com.axis.team2.technotribe.krasvbank.entity.User;
import com.axis.team2.technotribe.krasvbank.repository.TransactionRepository;
import com.axis.team2.technotribe.krasvbank.repository.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReportingService {

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private UserRepository userRepository;

	// USER

//Method to fetch all transactions by user
//	@Cacheable(cacheNames = "transactions", key = "#accountNumber")
	public List<Transaction> getAllTransactions(String accountNumber) {

		return transactionRepository.findByAccountNumber(accountNumber);

	}

	@Cacheable(cacheNames = "transactions", key = "#accountNumber")
	public TransactionReport getAllCredits(String accountNumber) {
		List<Transaction> transactions = transactionRepository.findByAccountNumberAndTransactionType(accountNumber,
				"CREDIT");
		BigDecimal totalAmount = transactions.stream().map(Transaction::getAmount).reduce(BigDecimal.ZERO,
				BigDecimal::add);
		return new TransactionReport(transactions, totalAmount);
	}

	// Method to fetch all debits by user
	@Cacheable(cacheNames = "transactions", key = "#accountNumber")
	public TransactionReport getAllDebits(String accountNumber) {
		List<Transaction> transactions = transactionRepository.findByAccountNumberAndTransactionType(accountNumber,
				"DEBIT");
		BigDecimal totalAmount = transactions.stream().map(Transaction::getAmount).reduce(BigDecimal.ZERO,
				BigDecimal::add);
		return new TransactionReport(transactions, totalAmount);
	}

	// Method to fetch all transfers by user
	@Cacheable(cacheNames = "transactions", key = "#accountNumber")
	public TransactionReport getAllFundTransfers(String accountNumber) {
		List<Transaction> transactions = transactionRepository.findByAccountNumberAndTransactionType(accountNumber,
				"TRANSFER");
		BigDecimal totalAmount = transactions.stream().map(Transaction::getAmount).reduce(BigDecimal.ZERO,
				BigDecimal::add);
		return new TransactionReport(transactions, totalAmount);
	}

	// Method to fetch all Credits for Current Month

	public TransactionReport getAllCreditsForCurrentMonth(String accountNumber) {
		YearMonth currentMonth = YearMonth.now();
		LocalDateTime startOfMonth = currentMonth.atDay(1).atStartOfDay();
		LocalDateTime endOfMonth = currentMonth.atEndOfMonth().atTime(23, 59, 59);

		List<Transaction> transactions = transactionRepository.findByAccountNumberAndTransactionTypeAndCreatedAtBetween(
				accountNumber, "CREDIT", startOfMonth, endOfMonth);
		BigDecimal totalAmount = transactions.stream().map(Transaction::getAmount).reduce(BigDecimal.ZERO,
				BigDecimal::add);

		return new TransactionReport(transactions, totalAmount);
	}

	// Method to fetch all Debits for Current Month
	public TransactionReport getAllDebitsForCurrentMonth(String accountNumber) {
		YearMonth currentMonth = YearMonth.now();
		LocalDateTime startOfMonth = currentMonth.atDay(1).atStartOfDay();
		LocalDateTime endOfMonth = currentMonth.atEndOfMonth().atTime(23, 59, 59);

		List<Transaction> transactions = transactionRepository.findByAccountNumberAndTransactionTypeAndCreatedAtBetween(
				accountNumber, "DEBIT", startOfMonth, endOfMonth);
		BigDecimal totalAmount = transactions.stream().map(Transaction::getAmount).reduce(BigDecimal.ZERO,
				BigDecimal::add);

		return new TransactionReport(transactions, totalAmount);
	}

	// ADMIN

//Method to fetch all transactions within a specified time frame for all users
	public TransactionReport getAllTransactionsWithinTimeframe(LocalDateTime startDate, LocalDateTime endDate) {
		List<Transaction> transactions = transactionRepository.findByCreatedAtBetween(startDate, endDate);
		BigDecimal totalAmount = transactions.stream().map(Transaction::getAmount).reduce(BigDecimal.ZERO,
				BigDecimal::add);

		return new TransactionReport(transactions, totalAmount);
	}

//Method to fetch all transactions within a specified timeframe for a specific user
	public TransactionReport getTransactionsByUserWithinTimeframe(String accountNumber, LocalDateTime startDate,
			LocalDateTime endDate) {
		List<Transaction> transactions = transactionRepository.findByAccountNumberAndCreatedAtBetween(accountNumber,
				startDate, endDate);
		BigDecimal totalAmount = transactions.stream().map(Transaction::getAmount).reduce(BigDecimal.ZERO,
				BigDecimal::add);

		return new TransactionReport(transactions, totalAmount);
	}

	// Method to fetch all transactions for a specific user
	@Cacheable(value = "transactions", key = "#accountNumber")
	public TransactionReport getAllTransactionsByUser(String accountNumber) {
		List<Transaction> transactions = transactionRepository.findByAccountNumber(accountNumber);
		BigDecimal totalAmount = transactions.stream().map(Transaction::getAmount).reduce(BigDecimal.ZERO,
				BigDecimal::add);

		return new TransactionReport(transactions, totalAmount);
	}

	// Method to fetch all credits for a specific user
	public TransactionReport getAllCreditsByUser(String accountNumber) {
		List<Transaction> transactions = transactionRepository.findByAccountNumberAndTransactionType(accountNumber,
				"CREDIT");
		BigDecimal totalAmount = transactions.stream().map(Transaction::getAmount).reduce(BigDecimal.ZERO,
				BigDecimal::add);

		return new TransactionReport(transactions, totalAmount);
	}

	// Method to fetch all debits for a specific user
	public TransactionReport getAllDebitsByUser(String accountNumber) {
		List<Transaction> transactions = transactionRepository.findByAccountNumberAndTransactionType(accountNumber,
				"DEBIT");
		BigDecimal totalAmount = transactions.stream().map(Transaction::getAmount).reduce(BigDecimal.ZERO,
				BigDecimal::add);

		return new TransactionReport(transactions, totalAmount);
	}

	// Method to fetch all fund transfers for a specific user
	@Cacheable(value = "transactions", key = "#accountNumber")
	public TransactionReport getAllFundTransfersByUser(String accountNumber) {
		List<Transaction> transactions = transactionRepository.findByAccountNumberAndTransactionType(accountNumber,
				"TRANSFER");
		BigDecimal totalAmount = transactions.stream().map(Transaction::getAmount).reduce(BigDecimal.ZERO,
				BigDecimal::add);

		return new TransactionReport(transactions, totalAmount);
	}

//Method to fetch users created within a specified timeframe
	public List<User> getUsersCreatedWithinTimeframe(LocalDateTime startDate, LocalDateTime endDate) {
		return userRepository.findByCreatedAtBetween(startDate, endDate);
	}

	// Method to Get All Active Users Count
	public long getCountOfActiveUsers() {
		return userRepository.countByStatus("Active");
	}

	// Method to Get All Inctive Users Count
	public long getCountOfInactiveUsers() {
		return userRepository.countByStatus("Inactive");
	}

	// Method to fetch all credits, debits and transfers of last 12 Months
//	public List<MonthlyTransactionSummary> getMonthlyTransactionSummary() {
//		List<MonthlyTransactionSummary> summaries = new ArrayList<>();
//		for (int i = 0; i < 12; i++) {
//			YearMonth month = YearMonth.now().minusMonths(i);
//			LocalDateTime startOfMonth = month.atDay(1).atStartOfDay();
//			LocalDateTime endOfMonth = month.atEndOfMonth().atTime(23, 59, 59);
//
//			BigDecimal totalCredits = transactionRepository
//					.findByTransactionTypeAndCreatedAtBetween("CREDIT", startOfMonth, endOfMonth).stream()
//					.map(Transaction::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
//
//			BigDecimal totalDebits = transactionRepository
//					.findByTransactionTypeAndCreatedAtBetween("DEBIT", startOfMonth, endOfMonth).stream()
//					.map(Transaction::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
//
//			BigDecimal totalTransfers = transactionRepository
//					.findByTransactionTypeAndCreatedAtBetween("TRANSFER", startOfMonth, endOfMonth).stream()
//					.map(Transaction::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
//
//			summaries.add(new MonthlyTransactionSummary(month, totalCredits, totalDebits, totalTransfers));
//		}
//		return summaries;
//	}

	public List<MonthlyTransactionSummary> getMonthlyTransactionSummary() {
		List<MonthlyTransactionSummary> summaries = new ArrayList<>();
		for (int i = 12; i > 0; i--) {
			YearMonth month = YearMonth.now().minusMonths(i);
			LocalDateTime startOfMonth = month.atDay(1).atStartOfDay();
			LocalDateTime endOfMonth = month.atEndOfMonth().atTime(23, 59, 59);

			BigDecimal totalCredits = transactionRepository
					.findByTransactionTypeAndCreatedAtBetween("CREDIT", startOfMonth, endOfMonth).stream()
					.map(Transaction::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

			BigDecimal totalDebits = transactionRepository
					.findByTransactionTypeAndCreatedAtBetween("DEBIT", startOfMonth, endOfMonth).stream()
					.map(Transaction::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

			BigDecimal totalTransfers = transactionRepository
					.findByTransactionTypeAndCreatedAtBetween("TRANSFER", startOfMonth, endOfMonth).stream()
					.map(Transaction::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

			summaries.add(new MonthlyTransactionSummary(month, totalCredits, totalDebits, totalTransfers));
		}
		return summaries;
	}

	// Method to fetch all credits, debits, and transfers of the current month
	public MonthlyTransactionSummary getCurrentMonthTransactionSummary() {
		YearMonth currentMonth = YearMonth.now();
		LocalDateTime startOfMonth = currentMonth.atDay(1).atStartOfDay();
		LocalDateTime endOfMonth = currentMonth.atEndOfMonth().atTime(23, 59, 59);

		BigDecimal totalCredits = transactionRepository
				.findByTransactionTypeAndCreatedAtBetween("CREDIT", startOfMonth, endOfMonth).stream()
				.map(Transaction::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

		BigDecimal totalDebits = transactionRepository
				.findByTransactionTypeAndCreatedAtBetween("DEBIT", startOfMonth, endOfMonth).stream()
				.map(Transaction::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

		BigDecimal totalTransfers = transactionRepository
				.findByTransactionTypeAndCreatedAtBetween("TRANSFER", startOfMonth, endOfMonth).stream()
				.map(Transaction::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

		return new MonthlyTransactionSummary(currentMonth, totalCredits, totalDebits, totalTransfers);
	}

	//// Method to fetch all credits, debits and transfers of last 12 Months for
	//// specific user
	public List<MonthlyTransactionSummary> getMonthlyTransactionSummaryForUser(String accountNumber) {
		List<MonthlyTransactionSummary> summaries = new ArrayList<>();
//	     for (int i = 0; i < 12; i++) {
		for (int i = 12; i > 0; i--) {
			YearMonth month = YearMonth.now().minusMonths(i);
			LocalDateTime startOfMonth = month.atDay(1).atStartOfDay();
			LocalDateTime endOfMonth = month.atEndOfMonth().atTime(23, 59, 59);

			BigDecimal totalCredits = transactionRepository
					.findByAccountNumberAndTransactionTypeAndCreatedAtBetween(accountNumber, "CREDIT", startOfMonth,
							endOfMonth)
					.stream().map(Transaction::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

			BigDecimal totalDebits = transactionRepository
					.findByAccountNumberAndTransactionTypeAndCreatedAtBetween(accountNumber, "DEBIT", startOfMonth,
							endOfMonth)
					.stream().map(Transaction::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

			BigDecimal totalTransfers = transactionRepository
					.findByAccountNumberAndTransactionTypeAndCreatedAtBetween(accountNumber, "TRANSFER", startOfMonth,
							endOfMonth)
					.stream().map(Transaction::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

			summaries.add(new MonthlyTransactionSummary(month, totalCredits, totalDebits, totalTransfers));
		}
		return summaries;
	}

	// Method to fetch all credits, debits and transfers of the current month for a
	// specific user
	public MonthlyTransactionSummary getCurrentMonthTransactionSummaryForUser(String accountNumber) {
		YearMonth currentMonth = YearMonth.now();
		LocalDateTime startOfMonth = currentMonth.atDay(1).atStartOfDay();
		LocalDateTime endOfMonth = currentMonth.atEndOfMonth().atTime(23, 59, 59);

		BigDecimal totalCredits = transactionRepository
				.findByAccountNumberAndTransactionTypeAndCreatedAtBetween(accountNumber, "CREDIT", startOfMonth,
						endOfMonth)
				.stream().map(Transaction::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

		BigDecimal totalDebits = transactionRepository
				.findByAccountNumberAndTransactionTypeAndCreatedAtBetween(accountNumber, "DEBIT", startOfMonth,
						endOfMonth)
				.stream().map(Transaction::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

		BigDecimal totalTransfers = transactionRepository
				.findByAccountNumberAndTransactionTypeAndCreatedAtBetween(accountNumber, "TRANSFER", startOfMonth,
						endOfMonth)
				.stream().map(Transaction::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

		return new MonthlyTransactionSummary(currentMonth, totalCredits, totalDebits, totalTransfers);
	}

	// ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
// Docker 

	public Transaction saveTransaction(Transaction transaction) {
		return transactionRepository.save(transaction);
	}

	public Optional<Transaction> getTransactionById(String transactionId) {
		return transactionRepository.findById(transactionId);
	}

	public List<Transaction> getAllTransactions() {
		return transactionRepository.findAll();
	}

	public Transaction updateTransaction(String transactionId, Transaction transactionDetails) {
		Transaction transaction = transactionRepository.findById(transactionId).orElseThrow(
				() -> new ResourceNotFoundException("Transaction not found for this id :: " + transactionId));
		transaction.setTransactionType(transactionDetails.getTransactionType());
		transaction.setAmount(transactionDetails.getAmount());
		transaction.setAccountNumber(transactionDetails.getAccountNumber());
		transaction.setStatus(transactionDetails.getStatus());
		return transactionRepository.save(transaction);
	}

	public void deleteTransaction(String transactionId) {
		Transaction transaction = transactionRepository.findById(transactionId).orElseThrow(
				() -> new ResourceNotFoundException("Transaction not found for this id :: " + transactionId));
		transactionRepository.delete(transaction);
	}

}
