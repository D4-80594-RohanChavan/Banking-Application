package com.app.team2.technotribe.krasvbank.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BankResponse implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String responseCode;
	private String responseMessage;
	private AccountInfo accountInfo;
}
