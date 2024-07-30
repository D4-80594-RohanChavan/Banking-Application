package com.app.team2.technotribe.krasvbank.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountInfo implements Serializable{

	private static final long serialVersionUID = 1L;
	@Schema(name = "User Account Name")
	private String accountName;

	@Schema(name = "User Account Balance")
	private BigDecimal accountBalance;

	@Schema(name = "User Account Number")
	private String accountNumber;
}
