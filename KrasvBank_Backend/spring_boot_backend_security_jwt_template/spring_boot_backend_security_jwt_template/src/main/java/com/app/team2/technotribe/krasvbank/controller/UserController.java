package com.app.team2.technotribe.krasvbank.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.team2.technotribe.krasvbank.dto.BankResponse;
import com.app.team2.technotribe.krasvbank.dto.CreditDebitRequest;
import com.app.team2.technotribe.krasvbank.dto.EnquiryRequest;
import com.app.team2.technotribe.krasvbank.dto.SigninRequest;
import com.app.team2.technotribe.krasvbank.dto.SigninResponse;
import com.app.team2.technotribe.krasvbank.dto.TransferRequest;
import com.app.team2.technotribe.krasvbank.entity.User;
import com.app.team2.technotribe.krasvbank.dto.SignupRequest;
import com.app.team2.technotribe.krasvbank.service.impl.UserService;
import com.app.team2.technotribe.krasvbank.jwt_utils.JwtUtils;
import com.app.team2.technotribe.krasvbank.repository.TokenBlacklistRepository;
import com.app.team2.technotribe.krasvbank.repository.UserRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.bytebuddy.dynamic.DynamicType.Builder.FieldDefinition.Optional;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User Account Management APIs")
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

	@Operation(summary = "Create New User Account", description = "Creating a new user and assigning an account ID")
	@ApiResponse(responseCode = "201", description = "Http Status 201 Created")

	@PostMapping("/createAccount")
	public BankResponse createAccount(@RequestBody SignupRequest userRequest) {
		return userService.createAccount(userRequest);
	}
	 @PostMapping("/logout")
	    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader) {
	        if (authHeader != null && authHeader.startsWith("Bearer ")) {
	            String token = authHeader.substring(7);
	            tokenBlacklistRepository.add(token);
	        }
	        SecurityContextHolder.clearContext();
	        return ResponseEntity.ok("Logged out successfully");
	    }

	 @PostMapping("/signin")
	    public ResponseEntity<?> signIn(@RequestBody @Valid SigninRequest request) {
	        System.out.println("in sign in " + request);
	        Authentication principal = mgr
	                .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
	        String jwtToken = utils.generateJwtToken(principal);
	       
	      
	        return ResponseEntity.ok(new SigninResponse(jwtToken, "User authentication success!!!"));
	    }

	@Operation(summary = "Balance Enquiry", description = "Given an account number, cheak how much the user has")
	@ApiResponse(responseCode = "201", description = "Http Status 201 SUCCESS")
	@GetMapping("/balanceEnquiry")
	public BankResponse balanceEnquiry(@RequestBody EnquiryRequest request) {
		return userService.balanceEnquiry(request);
	}

	@GetMapping("nameEnquiry")
	public String nameEnquiry(@RequestBody EnquiryRequest request) {
		return userService.nameEnquiry(request);
	}

	@PostMapping("credit")
	public BankResponse creditAccount(@RequestBody CreditDebitRequest request) {
		return userService.creditAccount(request);
	}

	@PostMapping("debit")
	public BankResponse debitAccount(@RequestBody CreditDebitRequest request) {
		return userService.debitAccount(request);
	}

	@PostMapping("transfer")
	public BankResponse transfer(@RequestBody TransferRequest request) {
		return userService.transfer(request);
	}

}
