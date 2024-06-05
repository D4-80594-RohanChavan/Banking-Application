package com.app.team2.technotribe.krasvbank.dto;



//import com.fasterxml.jackson.annotation.JsonProperty;
//import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Getter;
import com.app.team2.technotribe.krasvbank.entity.UserRole;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {

//	@JsonProperty(access = Access.READ_ONLY)
//	private Long id;
	private String name;
	private String gender;
	private String address;
	private String stateOfOrigin;
	private String email;
	private String password;
	private String phoneNumber;
	private String alternativePhoneNumber;
	private UserRole role;

}
