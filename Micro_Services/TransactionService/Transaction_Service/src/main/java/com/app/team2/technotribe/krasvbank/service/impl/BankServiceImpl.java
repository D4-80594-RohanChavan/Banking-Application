package com.app.team2.technotribe.krasvbank.service.impl;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.team2.technotribe.krasvbank.dto.AccountInfo;
import com.app.team2.technotribe.krasvbank.dto.BankResponse;
import com.app.team2.technotribe.krasvbank.dto.CreditDebitRequest;
import com.app.team2.technotribe.krasvbank.dto.EmailDetails;
import com.app.team2.technotribe.krasvbank.dto.EnquiryRequest;
import com.app.team2.technotribe.krasvbank.dto.TransactionDto;
import com.app.team2.technotribe.krasvbank.dto.TransferRequest;
import com.app.team2.technotribe.krasvbank.entity.Transaction;
import com.app.team2.technotribe.krasvbank.entity.BankAccount;
import com.app.team2.technotribe.krasvbank.repository.BankRepository;
import com.app.team2.technotribe.krasvbank.util.AccountUtils;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BankServiceImpl implements BankService {

	@Autowired
	BankRepository bankRepository;

	@Autowired
	EmailService emailService;

	@Autowired
	TransactionService transactionService;

	@Override
	public BigDecimal balanceEnquiry(EnquiryRequest request) {
		// check if the provided account number exist in db
		System.out.println(request.getAccountNumber());
		BankAccount foundUser = bankRepository.findByAccountNumber(request.getAccountNumber());

		return foundUser.getAccountBalance();

	}

	@Override
	public String nameEnquiry(EnquiryRequest request) {
		// check if the provided account number exist in db
		boolean isAccountExist = bankRepository.existsByAccountNumber(request.getAccountNumber());
		if (!isAccountExist) {
			return AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE;
		}
		BankAccount foundUser = bankRepository.findByAccountNumber(request.getAccountNumber());
		System.out.println("inside userservice" + foundUser.toString());
		return foundUser.getName();
	}

	@Transactional
	@Override
	public BankResponse creditAccount(CreditDebitRequest request) {

		boolean isAccountExist = bankRepository.existsByAccountNumber(request.getAccountNumber());
		if (!isAccountExist) {
			return BankResponse.builder().responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
					.responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE).accountInfo(null).build();
		}

		BankAccount userToCredit = bankRepository.findByAccountNumber(request.getAccountNumber());
		userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(request.getAmount()));
		bankRepository.save(userToCredit);

		// Save transaction
		TransactionDto transactionDto = TransactionDto.builder().accountNumber(userToCredit.getAccountNumber())
				.transactionType("CREDIT").amount(request.getAmount()).status("SUCCESS").build();

		transactionService.saveTransaction(transactionDto);

		// send Email
		EmailDetails creditAlert = EmailDetails.builder().subject("Credit Alert").recipient(userToCredit.getEmail())
				.messageBody(request.getAmount() + " has been Credited to your account !").build();
		emailService.sendEmailAlert(creditAlert);

		return BankResponse.builder().responseCode(AccountUtils.ACCOUNT_CREDITED_SUCCESS_CODE)
				.responseMessage(AccountUtils.ACCOUNT_CREDITED_SUCCESS_MESSAGE)
				.accountInfo(AccountInfo.builder().accountName(userToCredit.getName())
						.accountBalance(userToCredit.getAccountBalance()).accountNumber(request.getAccountNumber())
						.build())
				.build();
	}

	@Override
	public BankResponse debitAccount(CreditDebitRequest request) {

		// cheak Account if exists
		boolean isAccountExist = bankRepository.existsByAccountNumber(request.getAccountNumber());
		if (!isAccountExist) {
			return BankResponse.builder().responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
					.responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE).accountInfo(null).build();
		}

		// cheak the amount, debit amount is greater than balance
		BankAccount userToDebit = bankRepository.findByAccountNumber(request.getAccountNumber());

		BigDecimal availableBalance = userToDebit.getAccountBalance();
		BigDecimal debitAmount = request.getAmount();

		if (availableBalance.compareTo(debitAmount) < 0) {
			return BankResponse.builder().responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
					.responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE).accountInfo(null).build();
		}

		else {
			userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(request.getAmount()));
			bankRepository.save(userToDebit);

			// Save transaction
			TransactionDto transactionDto = TransactionDto.builder().accountNumber(userToDebit.getAccountNumber())
					.transactionType("DEBIT").amount(request.getAmount()).status("SUCCESS").build();

			transactionService.saveTransaction(transactionDto);

			EmailDetails creditAlert = EmailDetails.builder().subject("Debit Alert").recipient(userToDebit.getEmail())
					.messageBody(request.getAmount() + " has Debited from your account !").build();
			emailService.sendEmailAlert(creditAlert);
			return BankResponse.builder().responseCode(AccountUtils.ACCOUNT_DEBITED_SUCCESS_CODE)
					.responseMessage(AccountUtils.ACCOUNT_DEBITED_SUCCESS_MESSAGE)
					.accountInfo(AccountInfo.builder().accountNumber(request.getAccountNumber())
							.accountName(userToDebit.getName()).accountBalance(userToDebit.getAccountBalance()).build())
					.build();
		}
	}

	@Transactional
	@Override
	public BankResponse transfer(TransferRequest request) {
		// get the account to debit
		// cheak Account if exists
		boolean isDestinationAccountExist = bankRepository.existsByAccountNumber(request.getDestinationAccountNumber());

		if (!isDestinationAccountExist) {
			return BankResponse.builder().responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
					.responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE).accountInfo(null).build();
		}
		// cheak if the amount i'm debiting is not more than the current balance
		// debit the account
		BankAccount sourceAccountUser = bankRepository.findByAccountNumber(request.getSourceAccountNumber());

		if (request.getAmount().compareTo(sourceAccountUser.getAccountBalance()) > 0) {
			return BankResponse.builder().responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
					.responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE).accountInfo(null).build();
		}
		sourceAccountUser.setAccountBalance(sourceAccountUser.getAccountBalance().subtract(request.getAmount()));
		String sourceUsername = sourceAccountUser.getName();
		bankRepository.save(sourceAccountUser);

		EmailDetails debitAlert = EmailDetails.builder().subject("Debit Alert").recipient(sourceAccountUser.getEmail())
				.messageBody(request.getAmount() + " has been deducted from your account ! Your current balance is "
						+ sourceAccountUser.getAccountBalance())
				.build();

		emailService.sendEmailAlert(debitAlert);

		BankAccount destinationAccountUser = bankRepository.findByAccountNumber(request.getDestinationAccountNumber());
		destinationAccountUser.setAccountBalance(destinationAccountUser.getAccountBalance().add(request.getAmount()));
		bankRepository.save(destinationAccountUser);

		EmailDetails creditAlert = EmailDetails.builder().subject("Credit Alert")
				.recipient(destinationAccountUser.getEmail())
				.messageBody(request.getAmount() + " has been Credited to your account from!" + sourceUsername
						+ " Your current balance is " + destinationAccountUser.getAccountBalance())
				.build();
		emailService.sendEmailAlert(creditAlert);

		// Save transaction
		TransactionDto transactionDto = TransactionDto.builder()
				.accountNumber(destinationAccountUser.getAccountNumber()).transactionType("Account Transfer")
				.amount(request.getAmount()).status("SUCCESS").build();

		transactionService.saveTransaction(transactionDto);

		return BankResponse.builder().responseCode(AccountUtils.TRANSFER_SUCCESSFUL_CODE)
				.responseMessage(AccountUtils.TRANSFER_SUCCESSFUL_MESSAGE).accountInfo(null).build();
	}

	@Override
	public String createAccount(BankAccount newAccount) {

		bankRepository.save(newAccount);

		// send Email
		EmailDetails creditAlert = EmailDetails.builder().subject("Congratulations !!!! Your Bank Account is Activated")
				.recipient(newAccount.getEmail()).messageBody(newAccount.getName() + " your Account number is : "
						+ newAccount.getAccountNumber() + "  YOU CAN START BANKING")
				.build();
		emailService.sendEmailAlert(creditAlert);

		return "User is Active";
	}

}
