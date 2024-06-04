package com.app.team2.technotribe.krasvbank.service.impl;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.team2.technotribe.krasvbank.dto.AccountInfo;
import com.app.team2.technotribe.krasvbank.dto.BankResponse;
import com.app.team2.technotribe.krasvbank.dto.CreditDebitRequest;
//import com.app.team2.technotribe.krasvbank.dto.EmailDetails;
import com.app.team2.technotribe.krasvbank.dto.EnquiryRequest;
import com.app.team2.technotribe.krasvbank.dto.UserRequest;
import com.app.team2.technotribe.krasvbank.entity.User;
import com.app.team2.technotribe.krasvbank.repository.UserRepository;
import com.app.team2.technotribe.krasvbank.util.AccountUtils;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

//	@Autowired
//	EmailService emailService;

	@Override
	public BankResponse createAccount(UserRequest userRequest) {

		if (userRepository.existsByEmail(userRequest.getEmail())) {
			return BankResponse.builder().responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
					.responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE).accountInfo(null).build();
		}
		// creating account - saving new user
		User newUser = User.builder().name(userRequest.getName()).gender(userRequest.getGender())
				.address(userRequest.getAddress()).stateOfOrigin(userRequest.getStateOfOrigin())
				.accountNumber(AccountUtils.generateAccountNumber()).accountBalance(BigDecimal.ZERO)
				.email(userRequest.getEmail()).phoneNumber(userRequest.getPhoneNumber())
				.alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
				.status("ACTIVE")
				.build();

		User savedUser = userRepository.save(newUser);
		// Send email alert
//		EmailDetails emailDetails = EmailDetails.builder()
//				.recipient(savedUser.getEmail())
//				.subject("ACCOUNT CREATION")
//				.messageBody(
//						"Congratulations! Your Account Has been Successfully Created. \n Your Account Details:  \n Account Name:"
//								+ savedUser.getName() + "\n Account Number :" + savedUser.getAccountNumber())
//				.build();
//		emailService.sendEmailAlert(emailDetails);

		return BankResponse.builder().responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS)
				.responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
				.accountInfo(AccountInfo.builder().accountBalance(savedUser.getAccountBalance())
						.accountNumber(savedUser.getAccountNumber()).accountName(savedUser.getName()).build())
				.build();

	}

	@Override
	public BankResponse balanceEnquiry(EnquiryRequest request) {
		// check if the provided account number exist in db
		boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
		if (!isAccountExist) {
			return BankResponse.builder()
					.responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
					.responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
					.accountInfo(null)
					.build();
		}
		User foundUser = userRepository.findByAccountNumber(request.getAccountNumber());
		return BankResponse.builder()
				.responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
				.responseMessage(AccountUtils.ACCOUNT_FOUND_MESSAGE)
				.accountInfo(AccountInfo.builder().accountBalance(foundUser.getAccountBalance())
						.accountNumber(request.getAccountNumber()).accountName(foundUser.getName()).build())
				.build();
	}

	@Override
	public String nameEnquiry(EnquiryRequest request) {
		// check if the provided account number exist in db
		boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
		if (!isAccountExist) {
			return AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE;
		}
		User foundUser = userRepository.findByAccountNumber(request.getAccountNumber());
		System.out.println("inside userservice" + foundUser.toString());
		return foundUser.getName();
	}

	@Transactional
	@Override
	public BankResponse creditAccount(CreditDebitRequest request) {

		boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
		if(!isAccountExist) {
				return BankResponse.builder()
						.responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
						.responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
						.accountInfo(null)
						.build();
		}
	
		User userToCredit= userRepository.findByAccountNumber(request.getAccountNumber());
	
		userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(request.getAmount()));
		userRepository.save(userToCredit);
		return BankResponse.builder()
				.responseCode(AccountUtils.ACCOUNT_CREDITED_SUCCESS_CODE)
				.responseMessage(AccountUtils.ACCOUNT_CREDITED_SUCCESS_MESSAGE)
				.accountInfo(AccountInfo.builder()
						.accountName(userToCredit.getName()) 
						.accountBalance(userToCredit.getAccountBalance())
						.accountNumber(request.getAccountNumber())
						.build())
				.build();
	}

//	public BankResponse debitAccount(CreditDebitRequest request) {}
//
//	// balance Enquiry, name Enquiry, credit, debit, transfer

	
	@Override
	public BankResponse debitAccount(CreditDebitRequest request) {

		
		//cheak Account if exists
		boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
		if (!isAccountExist) {
			return BankResponse.builder()
					.responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
					.responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
					.accountInfo(null)
					.build();
		}
		
		//cheak the amount, debit amount is greater than balance 
		User userToDebit=userRepository.findByAccountNumber(request.getAccountNumber());
//		int availableBalance=Integer.parseInt(userToDebit.getAccountBalance().toString());
//		int debitAmount=Integer.parseInt(request.getAmount().toString());
//		if(availableBalance < debitAmount) {
		  BigDecimal availableBalance = userToDebit.getAccountBalance();
		    BigDecimal debitAmount = request.getAmount();

		    if (availableBalance.compareTo(debitAmount) < 0) {
		    	return BankResponse.builder()
					.responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
					.responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
					.accountInfo(null)
					.build();
		}
		else {
			userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(request.getAmount()));
			userRepository.save(userToDebit);
			return BankResponse.builder()
					.responseCode(AccountUtils.ACCOUNT_DEBITED_SUCCESS_CODE)
					.responseMessage(AccountUtils.ACCOUNT_DEBITED_SUCCESS_MESSAGE)
					.accountInfo(AccountInfo.builder()
							.accountNumber(request.getAccountNumber())
							.accountName(userToDebit.getName())
							.accountBalance(userToDebit.getAccountBalance())
							.build())
					.build();
		}
		//if(userToDebit.getAccountBalance().compareTo(request.getAmount()));
	
	}


}
