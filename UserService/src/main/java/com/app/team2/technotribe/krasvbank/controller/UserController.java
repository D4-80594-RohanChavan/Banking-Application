package com.app.team2.technotribe.krasvbank.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.team2.technotribe.krasvbank.Security.JwtUtils;
import com.app.team2.technotribe.krasvbank.bankStatement.BankStatement;
import com.app.team2.technotribe.krasvbank.dto.BankResponse;
import com.app.team2.technotribe.krasvbank.dto.CreditDebitRequest;
import com.app.team2.technotribe.krasvbank.dto.EnquiryRequest;
import com.app.team2.technotribe.krasvbank.dto.SigninRequest;
import com.app.team2.technotribe.krasvbank.dto.SigninResponse;
import com.app.team2.technotribe.krasvbank.dto.TransferRequest;
import com.app.team2.technotribe.krasvbank.entity.Transaction;
import com.app.team2.technotribe.krasvbank.entity.User;
import com.app.team2.technotribe.krasvbank.dto.SignupRequest;
import com.app.team2.technotribe.krasvbank.service.impl.UserService;
import com.app.team2.technotribe.krasvbank.util.AccountUtils;
import com.app.team2.technotribe.krasvbank.repository.TokenBlacklistRepository;
import com.app.team2.technotribe.krasvbank.repository.UserRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController

@RequestMapping("/api/user")
@Tag(name = "User Account Management APIs")
@CrossOrigin("*")
public class UserController {

	@Autowired
	private JwtUtils utils;
	@Autowired
	UserService userService;
	@Autowired
	UserRepository userRepository;
	@Autowired
	private TokenBlacklistRepository tokenBlacklistRepository;
	@Autowired
	private AuthenticationManager mgr;

	@Autowired
	private BankStatement bankStatement;

	@GetMapping("bankStatement")
	public List<Transaction> generateBankStatemant(@RequestParam String accountNumber, @RequestParam String startDate,
			@RequestParam String endDate) {
		return bankStatement.generateStatement(accountNumber, startDate, endDate);
	}

	@Operation(summary = "Create New User Account", description = "Creating a new user and assigning an account ID")
	@ApiResponse(responseCode = "201", description = "Http Status 201 Created")

	@PostMapping("createAccount")
	public BankResponse createAccount(@Valid @RequestBody SignupRequest userRequest) {
		return userService.createAccount(userRequest);
	}

	@PostMapping("logout")
	public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader) {
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			String token = authHeader.substring(7);
			tokenBlacklistRepository.add(token);
		}
		SecurityContextHolder.clearContext();
		return ResponseEntity.ok("Logged out successfully");
	}

	@PostMapping("signin")
	public ResponseEntity<?> signIn(@RequestBody @Valid SigninRequest request) {
		System.out.println("in sign in " + request);
		Authentication principal = mgr
				.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
		String jwtToken = utils.generateJwtToken(principal);
		String accnum = request.getAccountNumber();
		User foundUser = userRepository.findByAccountNumber(accnum);

		System.out.println("inside signin controller" + foundUser.toString());

		return ResponseEntity.ok(new SigninResponse(jwtToken, "User authentication success!!!", foundUser.getName(),
				accnum, foundUser.getGender(), foundUser.getEmail(), foundUser.getPhoneNumber(),
				foundUser.getAlternativePhoneNumber(), foundUser.getStateOfOrigin(), foundUser.getAddress(),
				foundUser.getRole()));
	}


	@PostMapping("balanceEnquiry")
	public ResponseEntity<BankResponse> balanceEnquiry(@RequestBody EnquiryRequest request) {
	    BankResponse response = userService.balanceEnquiry(request);
	    if (response.getResponseCode().equals(AccountUtils.ACCOUNT_FOUND_CODE)) {
	        return ResponseEntity.ok(response);
	    } else {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	    }
	}

	@GetMapping("nameEnquiry")
	public String nameEnquiry(@RequestBody EnquiryRequest request) {
		return userService.nameEnquiry(request);
	}


	@PostMapping("credit")
    public ResponseEntity<BankResponse> creditAccount(@RequestBody CreditDebitRequest request) {
        BankResponse response = userService.creditAccount(request);

        if (response.getResponseCode().equals(AccountUtils.ACCOUNT_NOT_EXIST_CODE)) {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } else if (response.getResponseCode().equals(AccountUtils.INCORRECT_PASSWORD_CODE)) {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

	@PostMapping("debit")
    public ResponseEntity<BankResponse> debitAccount(@RequestBody CreditDebitRequest request) {
        BankResponse response = userService.debitAccount(request);

        if (response.getResponseCode().equals(AccountUtils.ACCOUNT_NOT_EXIST_CODE)) {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } else if (response.getResponseCode().equals(AccountUtils.INCORRECT_PASSWORD_CODE)) {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

	@PostMapping("transfer")
    public ResponseEntity<BankResponse> transfer(@RequestBody TransferRequest request) {
        BankResponse response = userService.transfer(request);

        HttpStatus httpStatus;
        switch (response.getResponseCode()) {
            case AccountUtils.ACCOUNT_NOT_EXIST_CODE:
                httpStatus = HttpStatus.NOT_FOUND;
                break;
            case AccountUtils.INCORRECT_PASSWORD_CODE:
                httpStatus = HttpStatus.NOT_FOUND;
                break;
            default:
                httpStatus = HttpStatus.OK;
                break;
        }

        return ResponseEntity.status(httpStatus).body(response);
    }

}
