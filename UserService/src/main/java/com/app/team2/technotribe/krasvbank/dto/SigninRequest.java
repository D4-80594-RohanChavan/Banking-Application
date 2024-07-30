package com.app.team2.technotribe.krasvbank.dto;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import lombok.Getter;

import lombok.Setter;
import lombok.ToString;

//@Data
//@NoArgsConstructor
//@AllArgsConstructor
@Getter
@Setter
@ToString
public class SigninRequest {
	
	public String accountNumber;
	@NotBlank(message = "Email can't be blank")
	@Email(message = "Invalid email format")
	private String email;
	@Length(min =3,max=20,message = "Invalid password length")
	private String password;
}
