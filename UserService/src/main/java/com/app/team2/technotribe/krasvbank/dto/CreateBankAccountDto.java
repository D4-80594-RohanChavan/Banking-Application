package com.app.team2.technotribe.krasvbank.dto;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class CreateBankAccountDto {

	private String name;
	private String accountNumber;
	private BigDecimal accountBalance=BigDecimal.ZERO;
	private String email;
	private String password;
	private String phoneNumber;
	private String alternativePhoneNumber;

}
