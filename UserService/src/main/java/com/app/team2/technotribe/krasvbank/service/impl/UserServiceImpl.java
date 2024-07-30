package com.app.team2.technotribe.krasvbank.service.impl;

import java.math.BigDecimal;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.team2.technotribe.krasvbank.Security.JwtUtils;
import com.app.team2.technotribe.krasvbank.dto.AccountInfo;
import com.app.team2.technotribe.krasvbank.dto.BankResponse;
import com.app.team2.technotribe.krasvbank.dto.CreditDebitRequest;
import com.app.team2.technotribe.krasvbank.dto.EnquiryRequest;
import com.app.team2.technotribe.krasvbank.dto.TransferRequest;
import com.app.team2.technotribe.krasvbank.dto.SignupRequest;
import com.app.team2.technotribe.krasvbank.entity.User;
import com.app.team2.technotribe.krasvbank.external.services.ExternalTransactionService;
import com.app.team2.technotribe.krasvbank.repository.UserRepository;
import com.app.team2.technotribe.krasvbank.util.AccountUtils;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	ExternalTransactionService externalTransactionService;

	@Override
	public BankResponse createAccount(SignupRequest userRequest) {

		if (userRepository.existsByEmail(userRequest.getEmail())) {
			return BankResponse.builder().responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
					.responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE).accountInfo(null).build();
		}
		// creating account - saving new user
		User newUser = User.builder()
				.name(userRequest.getName())
				.gender(userRequest.getGender())
				.address(userRequest.getAddress())
				.stateOfOrigin(userRequest.getStateOfOrigin())
				.accountNumber(AccountUtils.generateAccountNumber())
				.email(userRequest.getEmail())
				.password(passwordEncoder.encode(userRequest.getPassword()))
				.phoneNumber(userRequest.getPhoneNumber())
				.alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
				.status("INACTIVE")
				.role(userRequest.getRole())
				.aadharCard(userRequest.getAadharCard())
				.panCard(userRequest.getPanCard())
				.build();

		User savedUser = userRepository.save(newUser);

		return BankResponse.builder().responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS)
				.responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
				.accountInfo(AccountInfo.builder().accountBalance(BigDecimal.ZERO)
						.accountNumber(savedUser.getAccountNumber()).accountName(savedUser.getName()).build())
				.build();

	}


	@Override
	public BankResponse balanceEnquiry(EnquiryRequest request) {
	    User userToCheckBalance = userRepository.findByAccountNumber(request.getAccountNumber());

	    if (userToCheckBalance == null) {
	        return BankResponse.builder()
	            .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
	            .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
	            .build();
	    }

	    boolean isPasswordMatch = passwordEncoder.matches(request.getPassword(), userToCheckBalance.getPassword());
	    System.out.println(isPasswordMatch + " isPasswordMatch");

	    if (!isPasswordMatch) {
	        return BankResponse.builder()
	            .responseCode(AccountUtils.INCORRECT_PASSWORD_CODE)
	            .responseMessage(AccountUtils.INCORRECT_PASSWORD_MESSAGE)
	            .build();
	    }

	    BigDecimal balance = externalTransactionService.balanceEnquiry(request.getAccountNumber());
	    User foundUser = userRepository.findByAccountNumber(request.getAccountNumber());

	    return BankResponse.builder()
	        .responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
	        .responseMessage(AccountUtils.ACCOUNT_FOUND_MESSAGE)
	        .accountInfo(AccountInfo.builder()
	            .accountBalance(balance)
	            .accountNumber(request.getAccountNumber())
	            .accountName(foundUser.getName())
	            .build())
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
        if (!isAccountExist) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        User userToCredit = userRepository.findByAccountNumber(request.getAccountNumber());

        boolean isPasswordMatch = passwordEncoder.matches(request.getPassword(), userToCredit.getPassword());
        if (!isPasswordMatch) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.INCORRECT_PASSWORD_CODE)
                    .responseMessage(AccountUtils.INCORRECT_PASSWORD_MESSAGE)
                    .accountInfo(null)
                    .build();
        } else {
            return externalTransactionService.creditAccount(request);
        }
    }



	@Transactional
    @Override
    public BankResponse debitAccount(CreditDebitRequest request) {

        boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isAccountExist) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        User userToDebit = userRepository.findByAccountNumber(request.getAccountNumber());

        boolean isPasswordMatch = passwordEncoder.matches(request.getPassword(), userToDebit.getPassword());
        if (!isPasswordMatch) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.INCORRECT_PASSWORD_CODE)
                    .responseMessage(AccountUtils.INCORRECT_PASSWORD_MESSAGE)
                    .accountInfo(null)
                    .build();
        } else {
            return externalTransactionService.debitAccount(request);
        }
    }

	@Transactional
    @Override
    public BankResponse transfer(TransferRequest request) {
        // check if the destination account exists
        boolean isDestinationAccountExist = userRepository.existsByAccountNumber(request.getDestinationAccountNumber());
        boolean isSourceAccountExist = userRepository.existsByAccountNumber(request.getSourceAccountNumber());
        if (!isDestinationAccountExist) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        if (!isSourceAccountExist) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        // check if the amount to be debited is not more than the current balance
        // debit the account
        User sourceAccountUser = userRepository.findByAccountNumber(request.getSourceAccountNumber());

        boolean isPasswordMatch = passwordEncoder.matches(request.getPassword(), sourceAccountUser.getPassword());

        if (!isPasswordMatch) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.INCORRECT_PASSWORD_CODE)
                    .responseMessage(AccountUtils.INCORRECT_PASSWORD_MESSAGE)
                    .accountInfo(null)
                    .build();
        } else {
            return externalTransactionService.transfer(request);
        }
    }

}
