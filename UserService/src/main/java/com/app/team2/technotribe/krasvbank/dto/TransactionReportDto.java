package com.app.team2.technotribe.krasvbank.dto;

import java.math.BigDecimal;
import java.util.List;

import com.app.team2.technotribe.krasvbank.entity.Transaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@AllArgsConstructor
public class TransactionReportDto {
	private List<Transaction> transactions;
	private BigDecimal totalAmount;
}
