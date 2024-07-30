package com.app.team2.technotribe.krasvbank.service.impl;

import java.beans.PropertyDescriptor;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.team2.technotribe.krasvbank.dto.CreateBankAccountDto;
import com.app.team2.technotribe.krasvbank.dto.GetUserByAccNumDto;
import com.app.team2.technotribe.krasvbank.dto.GetUserByStatusDto;
import com.app.team2.technotribe.krasvbank.dto.UpdateAccountStatusRequest;
import com.app.team2.technotribe.krasvbank.dto.UserDto;
import com.app.team2.technotribe.krasvbank.dto.UsersSummaryDto;
import com.app.team2.technotribe.krasvbank.entity.User;
import com.app.team2.technotribe.krasvbank.external.services.ExternalTransactionService;
import com.app.team2.technotribe.krasvbank.repository.UserRepository;

@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	UserRepository userRepo;

	@Autowired
	ExternalTransactionService externalTransactionService;


	// Method to fetch the summary of users
	@Override
	public UsersSummaryDto getUsersSummary() {
		int activeUsers = userRepo.countByStatus("Active");
		int inActiveUsers = userRepo.countByStatus("Inactive");
		int totalUsers = activeUsers + inActiveUsers;

		return new UsersSummaryDto(activeUsers, inActiveUsers, totalUsers);
	}

//	@Transactional
	@Override
	public String activateUser(UpdateAccountStatusRequest request) {
		User user = userRepo.findByAccountNumber(request.getAccountNumber());
		if (user != null) {
			user.setStatus("ACTIVE");
			userRepo.save(user);

			CreateBankAccountDto newacc = CreateBankAccountDto.builder().name(user.getName())
					.accountNumber(user.getAccountNumber()).accountBalance(BigDecimal.ZERO).email(user.getEmail())
					.password(user.getPassword()).phoneNumber(user.getPhoneNumber())
					.alternativePhoneNumber(user.getAlternativePhoneNumber()).build();

			return externalTransactionService.createAccount(newacc);

		}
		return "user not found";
	}

	@Override
	public List<UserDto> getUserByStatus(GetUserByStatusDto request) {
		return userRepo.findByStatus(request.getStatus()).stream().map(this::convertToDto).collect(Collectors.toList());
	}

	private UserDto convertToDto(User user) {
		BigDecimal balance = BigDecimal.ZERO;

		if ("ACTIVE".equals(user.getStatus())) {

			balance = externalTransactionService.balanceEnquiry(user.getAccountNumber());

		} else if ("INACTIVE".equals(user.getStatus())) {
			// Optionally handle ACTIVE status differently if needed
			balance = user.getAccountBalance(); // Assuming user.getAccountBalance() returns BigDecimal
		}

		return UserDto.builder().id(user.getId()).name(user.getName()).gender(user.getGender())
				.address(user.getAddress()).stateOfOrigin(user.getStateOfOrigin())
				.accountNumber(user.getAccountNumber()).accountBalance(balance).email(user.getEmail())
				.phoneNumber(user.getPhoneNumber()).alternativePhoneNumber(user.getAlternativePhoneNumber())
				.status(user.getStatus()).role(user.getRole()).createdAt(user.getCreatedAt())
				.modifiedAt(user.getModifiedAt()).aadharCard(user.getAadharCard()) // Added field
				.panCard(user.getPanCard()) // Added field
				.build();
	}

	@Override
	public User getUserByAccNum(GetUserByAccNumDto request) {
		// TODO Auto-generated method stub
		return userRepo.findByAccountNumber(request.getAccountNumber());
	}

	@Override
	public String inactiveUser(UpdateAccountStatusRequest request) {
		User user = userRepo.findByAccountNumber(request.getAccountNumber());
		if (user != null) {
			user.setStatus("INACTIVE");
			userRepo.save(user);
			// externalTransactionService.deleteAccount(request.getAccountNumber());
			return "User is Inactive";
		}
		return "user not found";
	}

	@Override
	public String updateUserDetails(User user) {
		String accountNumber = user.getAccountNumber(); // Get account number from the user object
		User existingUser = userRepo.findByAccountNumber(accountNumber);
		if (existingUser != null) {
			// Update only non-null fields from user to existingUser
			BeanUtils.copyProperties(user, existingUser, getNullPropertyNames(user));
			userRepo.save(existingUser);
			return "User Details Saved Successfully";
		}
		return "Invalid Account Number";
	}

	private String[] getNullPropertyNames(Object source) {
		final BeanWrapper src = new BeanWrapperImpl(source);
		Set<String> nullPropertyNames = new HashSet<>();
		for (PropertyDescriptor pd : src.getPropertyDescriptors()) {
			Object srcValue = src.getPropertyValue(pd.getName());
			if (srcValue == null)
				nullPropertyNames.add(pd.getName());
		}
		return nullPropertyNames.toArray(new String[nullPropertyNames.size()]);
	}


}
