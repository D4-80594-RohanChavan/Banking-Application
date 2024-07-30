package com.app.team2.technotribe.krasvbank.dto;

import java.math.BigDecimal;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class TransactionDto {

	private String transactionType;
	private BigDecimal amount;
	private String accountNumber;
	private String status="PENDING";
}
