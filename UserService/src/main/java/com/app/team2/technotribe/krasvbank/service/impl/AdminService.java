package com.app.team2.technotribe.krasvbank.service.impl;
import com.app.team2.technotribe.krasvbank.dto.UserDto;
import com.app.team2.technotribe.krasvbank.dto.UsersSummaryDto;

import java.util.List;

import com.app.team2.technotribe.krasvbank.dto.GetUserByAccNumDto;
import com.app.team2.technotribe.krasvbank.dto.GetUserByStatusDto;
import com.app.team2.technotribe.krasvbank.dto.MonthlyTransactionSummary;
import com.app.team2.technotribe.krasvbank.dto.UpdateAccountStatusRequest;
import com.app.team2.technotribe.krasvbank.entity.User;

public interface AdminService {

	String activateUser(UpdateAccountStatusRequest request);
	String inactiveUser(UpdateAccountStatusRequest request);

	List<UserDto> getUserByStatus(GetUserByStatusDto request);
	
	public User getUserByAccNum(GetUserByAccNumDto request);
	String updateUserDetails(User user);
	UsersSummaryDto getUsersSummary();

}
