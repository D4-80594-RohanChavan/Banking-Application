<<<<<<< HEAD
package com.app.team2.technotribe.krasvbank.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequest {

	private String password;
	private String sourceAccountNumber;
	private String destinationAccountNumber;
	private BigDecimal amount;
}
=======
package com.app.team2.technotribe.krasvbank.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequest {

	private String password;
	private String sourceAccountNumber;
	private String destinationAccountNumber;
	private BigDecimal amount;
}
>>>>>>> 1f121701983e37c5f071e9c7568ecab8f131e0af
