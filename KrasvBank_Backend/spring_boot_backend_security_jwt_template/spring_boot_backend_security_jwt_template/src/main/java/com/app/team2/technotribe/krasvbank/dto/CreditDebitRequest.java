package com.app.team2.technotribe.krasvbank.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditDebitRequest {
	private String password;
private String accountNumber;
private BigDecimal amount;
}
