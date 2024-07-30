package com.app.team2.technotribe.krasvbank.dto;

import java.math.BigDecimal;
import java.time.YearMonth;

public class MonthlyTransactionSummary {

	private YearMonth month;
	private BigDecimal totalCredits;
	private BigDecimal totalDebits;
	private BigDecimal totalTransfers;

	public MonthlyTransactionSummary(YearMonth month, BigDecimal totalCredits, BigDecimal totalDebits,
			BigDecimal totalTransfers) {
		this.month = month;
		this.totalCredits = totalCredits;
		this.totalDebits = totalDebits;
		this.totalTransfers = totalTransfers;
	}

	// Getters and Setters

	public YearMonth getMonth() {
		return month;
	}

	public void setMonth(YearMonth month) {
		this.month = month;
	}

	public BigDecimal getTotalCredits() {
		return totalCredits;
	}

	public void setTotalCredits(BigDecimal totalCredits) {
		this.totalCredits = totalCredits;
	}

	public BigDecimal getTotalDebits() {
		return totalDebits;
	}

	public void setTotalDebits(BigDecimal totalDebits) {
		this.totalDebits = totalDebits;
	}

	public BigDecimal getTotalTransfers() {
		return totalTransfers;
	}

	public void setTotalTransfers(BigDecimal totalTransfers) {
		this.totalTransfers = totalTransfers;
	}
}