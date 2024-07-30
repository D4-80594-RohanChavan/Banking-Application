package com.app.team2.technotribe.krasvbank.util;

import java.time.Year;

public class AccountUtils {
	public static final String ACCOUNT_EXISTS_CODE = "001";
	public static final String ACCOUNT_EXISTS_MESSAGE = "This user has already account created !";
	public static final String ACCOUNT_CREATION_SUCCESS = "002";
	public static final String ACCOUNT_CREATION_MESSAGE = "Account is under Verification ";
	  public static final String ACCOUNT_NOT_EXIST_CODE = "ACCOUNT_NOT_EXIST";
	    public static final String ACCOUNT_NOT_EXIST_MESSAGE = "Account does not exist.";
	    
	    public static final String INCORRECT_PASSWORD_CODE = "INCORRECT_PASSWORD";
	    public static final String INCORRECT_PASSWORD_MESSAGE = "Password does not match.";
	public static final String ACCOUNT_FOUND_CODE = "004";
	public static final String ACCOUNT_FOUND_MESSAGE = "User with provided Account Number exist";
	public static final String ACCOUNT_CREDITED_SUCCESS_CODE = "005";
	public static final String ACCOUNT_CREDITED_SUCCESS_MESSAGE = "User Account Credited Successfully";
	public static final String INSUFFICIENT_BALANCE_CODE = "006";
	public static final String INSUFFICIENT_BALANCE_MESSAGE = "Insufficient balance";
	public static final String ACCOUNT_DEBITED_SUCCESS_CODE = "200";
	public static final String ACCOUNT_DEBITED_SUCCESS_MESSAGE = "Amount has been successfully debited";
	
	public static final String TRANSFER_SUCCESSFUL_CODE = "200";
	public static final String TRANSFER_SUCCESSFUL_MESSAGE = "Transfer Successful";
	
	public static final String TRANSACTION_FAILED_CODE = "400";
    public static final String TRANSACTION_FAILED_MESSAGE = "Transaction failed";
	
	
	
	public static String generateAccountNumber() {
		Year currentYear = Year.now();
		int min = 100000;
		int max = 999999;

		// generate random number between min and max
		int randNumber = (int) Math.floor(Math.random() * (max - min + 1) + min);

		// convert the current and randomNunber to string , then concat

		String year = String.valueOf(currentYear);
		String randomNumber = String.valueOf(randNumber);
		StringBuilder accountNumber = new StringBuilder();

		return accountNumber.append(year).append(randomNumber).toString();
	}

}
