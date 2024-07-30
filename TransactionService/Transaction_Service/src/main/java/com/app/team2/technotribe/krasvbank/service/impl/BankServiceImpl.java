<<<<<<< HEAD
package com.app.team2.technotribe.krasvbank.service.impl;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.team2.technotribe.krasvbank.ExternalService.EmailClient;
import com.app.team2.technotribe.krasvbank.dto.AccountInfo;
import com.app.team2.technotribe.krasvbank.dto.BankResponse;
import com.app.team2.technotribe.krasvbank.dto.CreditDebitRequest;
import com.app.team2.technotribe.krasvbank.dto.EmailDetailsDto;
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

//	@Autowired
//	EmailService emailService;

	@Autowired
	private EmailClient emailService;

	@Autowired
	TransactionService transactionService;

	@Override
	public BigDecimal balanceEnquiry(EnquiryRequest request) {
		// check if the provided account number exist in db
		System.out.println(request.getAccountNumber());
		BankAccount foundUser = bankRepository.findByAccountNumber(request.getAccountNumber());
		if (foundUser == null) {
			throw new IllegalArgumentException("User not found with id: " + request.getAccountNumber());
		}
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
//		System.out.println("inside userservice" + foundUser.toString());
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
		EmailDetailsDto creditAlert = EmailDetailsDto.builder().subject("Credit Notification:"+ request.getAmount()+" credited to your account")
				.recipient(userToCredit.getEmail())
				.messageBody("We are writing to inform you that a credit transaction has been successfully processed on your account."
						+ "- Amount: "+request.getAmount() + " If you have any questions or notice any discrepancies, please contact our customer service immediately.\r\n"
								+ "\r\n"
								+ "Thank you for banking with us.\r\n"
								+ "\r\n"
								+ "Sincerely,\r\n"
								+ "Krasv Bank").build();
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

			EmailDetailsDto creditAlert = EmailDetailsDto.builder().subject("Debit Notification: "+request.getAmount()+" debited from your account")
					.recipient(userToDebit.getEmail())
					.messageBody("We would like to inform you that a debit transaction has been processed on your account.\r\n"
							+ "\r\n"
							
							+ "- Amount: "+request.getAmount()
									+ "\r\n"
									+ "Please ensure that this transaction aligns with your records. If you have any concerns, kindly reach out to our customer service.\r\n"
									+ "\r\n"
									+ "Thank you for choosing Krasv Bank.").build();
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
			System.out.println("Destination Account not Exist");
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

		EmailDetailsDto debitAlert = EmailDetailsDto.builder().subject("Funds Transfer Confirmation: "+request.getAmount()+" transferred from your Account")
				.recipient(sourceAccountUser.getEmail())
				.messageBody(request.getAmount() + " has been deducted from your account ! Your current balance is "
						+ sourceAccountUser.getAccountBalance()
						+ "\r\n"
						+ "If you did not authorize this transfer or have any questions, please contact our customer support team immediately.")
				.build();

		emailService.sendEmailAlert(debitAlert);

		BankAccount destinationAccountUser = bankRepository.findByAccountNumber(request.getDestinationAccountNumber());
		destinationAccountUser.setAccountBalance(destinationAccountUser.getAccountBalance().add(request.getAmount()));
		bankRepository.save(destinationAccountUser);

		EmailDetailsDto creditAlert = EmailDetailsDto.builder().subject("Funds Transfer Confirmation: "+request.getAmount()+" transferred to your Account")
				.recipient(destinationAccountUser.getEmail())
				.messageBody(request.getAmount() + " has been Credited to your account from!" + sourceUsername
						+ " Your current balance is " + destinationAccountUser.getAccountBalance()
						+ "\r\n"
						+ "If you did not authorize this transfer or have any questions, please contact our customer support team immediately.")
				.build();
		emailService.sendEmailAlert(creditAlert);

		// Save transaction
		TransactionDto transactionDto = TransactionDto.builder()
				.accountNumber(destinationAccountUser.getAccountNumber()).transactionType("TRANSFER")
				.amount(request.getAmount()).status("SUCCESS").build();

		transactionService.saveTransaction(transactionDto);

		return BankResponse.builder().responseCode(AccountUtils.TRANSFER_SUCCESSFUL_CODE)
				.responseMessage(AccountUtils.TRANSFER_SUCCESSFUL_MESSAGE).accountInfo(null).build();
	}

	@Override
	public String createAccount(BankAccount newAccount) {

		bankRepository.save(newAccount);

		// send Email
		EmailDetailsDto creditAlert = EmailDetailsDto.builder()
				.subject("Congratulations !!!! Your Bank Account is Activated").recipient(newAccount.getEmail())
				.messageBody(newAccount.getName() + " your Account number is : " + newAccount.getAccountNumber()
						+ "  YOU CAN START BANKING")
				.build();
		emailService.sendEmailAlert(creditAlert);

		return "User is Active";
	}

}
=======
package com.app.team2.technotribe.krasvbank.service.impl;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.team2.technotribe.krasvbank.ExternalService.EmailClient;
import com.app.team2.technotribe.krasvbank.dto.AccountInfo;
import com.app.team2.technotribe.krasvbank.dto.BankResponse;
import com.app.team2.technotribe.krasvbank.dto.CreditDebitRequest;
import com.app.team2.technotribe.krasvbank.dto.EmailDetailsDto;
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

//	@Autowired
//	EmailService emailService;

	@Autowired
	private EmailClient emailService;

	@Autowired
	TransactionService transactionService;

	@Override
	public BigDecimal balanceEnquiry(EnquiryRequest request) {
		// check if the provided account number exist in db
		System.out.println(request.getAccountNumber());
		BankAccount foundUser = bankRepository.findByAccountNumber(request.getAccountNumber());
		if (foundUser == null) {
			throw new IllegalArgumentException("User not found with id: " + request.getAccountNumber());
		}
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
//		System.out.println("inside userservice" + foundUser.toString());
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
		EmailDetailsDto creditAlert = EmailDetailsDto.builder().subject("Credit Notification:"+ request.getAmount()+" credited to your account")
				.recipient(userToCredit.getEmail())
				.messageBody("We are writing to inform you that a credit transaction has been successfully processed on your account."
						+ "- Amount: "+request.getAmount() + " If you have any questions or notice any discrepancies, please contact our customer service immediately.\r\n"
								+ "\r\n"
								+ "Thank you for banking with us.\r\n"
								+ "\r\n"
								+ "Sincerely,\r\n"
								+ "Krasv Bank").build();
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

			EmailDetailsDto creditAlert = EmailDetailsDto.builder().subject("Debit Notification: "+request.getAmount()+" debited from your account")
					.recipient(userToDebit.getEmail())
					.messageBody("We would like to inform you that a debit transaction has been processed on your account.\r\n"
							+ "\r\n"
							
							+ "- Amount: "+request.getAmount()
									+ "\r\n"
									+ "Please ensure that this transaction aligns with your records. If you have any concerns, kindly reach out to our customer service.\r\n"
									+ "\r\n"
									+ "Thank you for choosing Krasv Bank.").build();
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
			System.out.println("Destination Account not Exist");
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

		EmailDetailsDto debitAlert = EmailDetailsDto.builder().subject("Funds Transfer Confirmation: "+request.getAmount()+" transferred from your Account")
				.recipient(sourceAccountUser.getEmail())
				.messageBody(request.getAmount() + " has been deducted from your account ! Your current balance is "
						+ sourceAccountUser.getAccountBalance()
						+ "\r\n"
						+ "If you did not authorize this transfer or have any questions, please contact our customer support team immediately.")
				.build();

		emailService.sendEmailAlert(debitAlert);

		BankAccount destinationAccountUser = bankRepository.findByAccountNumber(request.getDestinationAccountNumber());
		destinationAccountUser.setAccountBalance(destinationAccountUser.getAccountBalance().add(request.getAmount()));
		bankRepository.save(destinationAccountUser);

		EmailDetailsDto creditAlert = EmailDetailsDto.builder().subject("Funds Transfer Confirmation: "+request.getAmount()+" transferred to your Account")
				.recipient(destinationAccountUser.getEmail())
				.messageBody(request.getAmount() + " has been Credited to your account from!" + sourceUsername
						+ " Your current balance is " + destinationAccountUser.getAccountBalance()
						+ "\r\n"
						+ "If you did not authorize this transfer or have any questions, please contact our customer support team immediately.")
				.build();
		emailService.sendEmailAlert(creditAlert);

		// Save transaction
		TransactionDto transactionDto = TransactionDto.builder()
				.accountNumber(destinationAccountUser.getAccountNumber()).transactionType("TRANSFER")
				.amount(request.getAmount()).status("SUCCESS").build();

		transactionService.saveTransaction(transactionDto);

		return BankResponse.builder().responseCode(AccountUtils.TRANSFER_SUCCESSFUL_CODE)
				.responseMessage(AccountUtils.TRANSFER_SUCCESSFUL_MESSAGE).accountInfo(null).build();
	}

	@Override
	public String createAccount(BankAccount newAccount) {

		bankRepository.save(newAccount);

		// send Email
		EmailDetailsDto creditAlert = EmailDetailsDto.builder()
				.subject("Congratulations !!!! Your Bank Account is Activated").recipient(newAccount.getEmail())
				.messageBody(newAccount.getName() + " your Account number is : " + newAccount.getAccountNumber()
						+ "  YOU CAN START BANKING")
				.build();
		emailService.sendEmailAlert(creditAlert);

		return "User is Active";
	}

}
>>>>>>> 1f121701983e37c5f071e9c7568ecab8f131e0af
