package com.app.team2.technotribe.krasvbank.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

	private String name;
	private String gender;
	private String address;
	private String stateOfOrigin;
	private String email;
	private String password;
	private String phoneNumber;
	private String alternativePhoneNumber;

}
