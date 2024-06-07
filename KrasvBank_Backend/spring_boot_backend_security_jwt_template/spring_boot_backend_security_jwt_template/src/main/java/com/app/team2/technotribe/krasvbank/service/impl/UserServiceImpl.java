package com.app.team2.technotribe.krasvbank.service.impl;

import java.math.BigDecimal;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.team2.technotribe.krasvbank.dto.AccountInfo;
import com.app.team2.technotribe.krasvbank.dto.BankResponse;
import com.app.team2.technotribe.krasvbank.dto.CreditDebitRequest;
import com.app.team2.technotribe.krasvbank.dto.EmailDetails;
import com.app.team2.technotribe.krasvbank.dto.EnquiryRequest;
import com.app.team2.technotribe.krasvbank.dto.TransactionDto;
import com.app.team2.technotribe.krasvbank.dto.TransferRequest;
import com.app.team2.technotribe.krasvbank.dto.SignupRequest;
import com.app.team2.technotribe.krasvbank.entity.User;
import com.app.team2.technotribe.krasvbank.jwt_utils.JwtUtils;
import com.app.team2.technotribe.krasvbank.repository.UserRepository;
import com.app.team2.technotribe.krasvbank.util.AccountUtils;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	EmailService emailService;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	TransactionService transactionService;

	@Override
	public BankResponse createAccount(SignupRequest userRequest) {

		if (userRepository.existsByEmail(userRequest.getEmail())) {
			return BankResponse.builder().responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
					.responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE).accountInfo(null).build();
		}
		// creating account - saving new user
		User newUser = User.builder().name(userRequest.getName()).gender(userRequest.getGender())
				.address(userRequest.getAddress()).stateOfOrigin(userRequest.getStateOfOrigin())
				.accountNumber(AccountUtils.generateAccountNumber()).accountBalance(BigDecimal.ZERO)
				.email(userRequest.getEmail()).password(passwordEncoder.encode(userRequest.getPassword()))
				.phoneNumber(userRequest.getPhoneNumber())
				.alternativePhoneNumber(userRequest.getAlternativePhoneNumber()).status("INACTIVE")
				.role(userRequest.getRole()).build();

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

//	public BankResponse login(SigninRequest loginDto) {
//		Authentication authentication=null;
//		authentication=authenticationManager.authenticate(
//				new UsernamePasswordAuthenticationToken(loginDto.getEmail(),loginDto.getPassword())
//				);
//		EmailDetails loginAlert=EmailDetails.builder()
//				.subject("You're logged in! ")
//				.recipient(loginDto.getEmail())
//				.messageBody("you logged into your account. if you did non initiate this request, please contact your bank")
//				.build();
//		emailService.sendEmailAlert(loginAlert);
//		return BankResponse.builder()
//				.responseCode("Login Success")
//				.responseMessage(jwtUtils.generateJwtToken(authentication))
//				.build();
//	}

	@Override
	public BankResponse balanceEnquiry(EnquiryRequest request) {
		// check if the provided account number exist in db
		boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
		if (!isAccountExist) {
			return BankResponse.builder().responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
					.responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE).accountInfo(null).build();
		}
		User foundUser = userRepository.findByAccountNumber(request.getAccountNumber());
		return BankResponse.builder().responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
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
		if (!isAccountExist) {
			return BankResponse.builder().responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
					.responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE).accountInfo(null).build();
		}

		User userToCredit = userRepository.findByAccountNumber(request.getAccountNumber());

		userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(request.getAmount()));
		userRepository.save(userToCredit);

		// Save transaction
		TransactionDto transactionDto = TransactionDto.builder().accountNumber(userToCredit.getAccountNumber())
				.transactionType("CREDIT").amount(request.getAmount()).build();

		transactionService.saveTransaction(transactionDto);

//		EmailDetails creditAlert=EmailDetails.builder()
//				.subject("Credit Alert")
//				.recipient(userToCredit.getEmail())
//				.messageBody(request.getAmount()+" has been Credited to your account !")
//				.build();
//		emailService.sendEmailAlert(creditAlert);

		return BankResponse.builder().responseCode(AccountUtils.ACCOUNT_CREDITED_SUCCESS_CODE)
				.responseMessage(AccountUtils.ACCOUNT_CREDITED_SUCCESS_MESSAGE)
				.accountInfo(AccountInfo.builder().accountName(userToCredit.getName())
						.accountBalance(userToCredit.getAccountBalance()).accountNumber(request.getAccountNumber())
						.build())
				.build();
	}

//	public BankResponse debitAccount(CreditDebitRequest request) {}
//
//	// balance Enquiry, name Enquiry, credit, debit, transfer

	@Override
	public BankResponse debitAccount(CreditDebitRequest request) {

		// cheak Account if exists
		boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
		if (!isAccountExist) {
			return BankResponse.builder().responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
					.responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE).accountInfo(null).build();
		}

		// cheak the amount, debit amount is greater than balance
		User userToDebit = userRepository.findByAccountNumber(request.getAccountNumber());

		BigDecimal availableBalance = userToDebit.getAccountBalance();
		BigDecimal debitAmount = request.getAmount();

		if (availableBalance.compareTo(debitAmount) < 0) {
			return BankResponse.builder().responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
					.responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE).accountInfo(null).build();
		}

		else {
			userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(request.getAmount()));
			userRepository.save(userToDebit);

			// Save transaction
			TransactionDto transactionDto = TransactionDto.builder().accountNumber(userToDebit.getAccountNumber())
					.transactionType("DEBIT").amount(request.getAmount()).build();

			transactionService.saveTransaction(transactionDto);

//			EmailDetails creditAlert=EmailDetails.builder()
//					.subject("Debit Alert")
//					.recipient(userToDebit.getEmail())
//					.messageBody(request.getAmount()+" has Debited from your account !")
//					.build();
//			emailService.sendEmailAlert(creditAlert);
			return BankResponse.builder().responseCode(AccountUtils.ACCOUNT_DEBITED_SUCCESS_CODE)
					.responseMessage(AccountUtils.ACCOUNT_DEBITED_SUCCESS_MESSAGE)
					.accountInfo(AccountInfo.builder().accountNumber(request.getAccountNumber())
							.accountName(userToDebit.getName()).accountBalance(userToDebit.getAccountBalance()).build())
					.build();
		}
		// if(userToDebit.getAccountBalance().compareTo(request.getAmount()));

	}

	@Transactional
	@Override
	public BankResponse transfer(TransferRequest request) {
		// get the account to debit
		// cheak Account if exists
		boolean isDestinationAccountExist = userRepository.existsByAccountNumber(request.getDestinationAccountNumber());

		if (!isDestinationAccountExist) {
			return BankResponse.builder().responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
					.responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE).accountInfo(null).build();
		}
		// cheak if the amount i'm debiting is not more than the current balance
		// debit the account
		User sourceAccountUser = userRepository.findByAccountNumber(request.getSourceAccountNumber());
		if (request.getAmount().compareTo(sourceAccountUser.getAccountBalance()) > 0) {
			return BankResponse.builder().responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
					.responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE).accountInfo(null).build();
		}
		sourceAccountUser.setAccountBalance(sourceAccountUser.getAccountBalance().subtract(request.getAmount()));
		String sourceUsername = sourceAccountUser.getName();
		userRepository.save(sourceAccountUser);

//				EmailDetails debitAlert=EmailDetails.builder()
//						.subject("Debit Alert")
//						.recipient(sourceAccountUser.getEmail())
//						.messageBody(request.getAmount()+" has been deducted from your account ! Your current balance is "+sourceAccountUser.getAccountBalance())
//						.build();
//				
//				emailService.sendEmailAlert(debitAlert);

		User destinationAccountUser = userRepository.findByAccountNumber(request.getDestinationAccountNumber());
		destinationAccountUser.setAccountBalance(destinationAccountUser.getAccountBalance().add(request.getAmount()));
		userRepository.save(destinationAccountUser);

//				EmailDetails creditAlert=EmailDetails.builder()
//						.subject("Credit Alert")
//						.recipient(destinationAccountUser.getEmail())
//						.messageBody(request.getAmount()+" has been Credited to your account from!"+sourceUsername+" Your current balance is "+destinationAccountUser.getAccountBalance())
//						.build();
//				emailService.sendEmailAlert(creditAlert);

		// Save transaction
		TransactionDto transactionDto = TransactionDto.builder()
				.accountNumber(destinationAccountUser.getAccountNumber()).transactionType("Account Transfer")
				.amount(request.getAmount()).build();

		transactionService.saveTransaction(transactionDto);

		return BankResponse.builder().responseCode(AccountUtils.TRANSFER_SUCCESSFUL_CODE)
				.responseMessage(AccountUtils.TRANSFER_SUCCESSFUL_MESSAGE).accountInfo(null).build();
	}

}
