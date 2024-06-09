package com.app.team2.technotribe.krasvbank.service.impl;

import java.util.List;

import com.app.team2.technotribe.krasvbank.dto.GetUserByAccNumDto;
import com.app.team2.technotribe.krasvbank.dto.GetUserByStatusDto;
import com.app.team2.technotribe.krasvbank.dto.UpdateAccountStatusRequest;
import com.app.team2.technotribe.krasvbank.entity.User;

public interface AdminService {

	String activateUser(UpdateAccountStatusRequest request);
	String inactiveUser(UpdateAccountStatusRequest request);

	List<User> getUserByStatus(GetUserByStatusDto request);
	
	public User getUserByAccNum(GetUserByAccNumDto request);
	String updateUserDetails(User user);

}
