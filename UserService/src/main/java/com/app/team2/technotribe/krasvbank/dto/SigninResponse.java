package com.app.team2.technotribe.krasvbank.dto;

import com.app.team2.technotribe.krasvbank.entity.UserRole;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SigninResponse {

	private String jwt;
	private String mesg;
	private String name;
	public String accountNumber;
	private String gender;
	private String email;
	private String phoneNumber;
	private String alternativePhoneNumber;
	private String stateOfOrigin;
	private String address;
	private UserRole role;
	
	


}
