package com.app.team2.technotribe.krasvbank.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.team2.technotribe.krasvbank.dto.GetUserByAccNumDto;
import com.app.team2.technotribe.krasvbank.dto.GetUserByStatusDto;
import com.app.team2.technotribe.krasvbank.dto.UpdateAccountStatusRequest;
import com.app.team2.technotribe.krasvbank.dto.UserDto;
import com.app.team2.technotribe.krasvbank.dto.UsersSummaryDto;
import com.app.team2.technotribe.krasvbank.entity.User;
import com.app.team2.technotribe.krasvbank.service.impl.AdminService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin("*")
public class AdminController {

	@Autowired
	AdminService adminService;


	@GetMapping("/users-summary")
	public UsersSummaryDto getUsersSummary() {
	    return adminService.getUsersSummary();
	}

	@PostMapping("/activateuser")
	@Operation(summary = "Api to update the bank account status")
	public String activateuser(@RequestBody UpdateAccountStatusRequest request) {
		return adminService.activateUser(request);
	}

	@PostMapping("/inactiveUser")
	@Operation(summary = "Api to update the bank account status")
	public String deactivateuser(@RequestBody UpdateAccountStatusRequest request) {
		return adminService.inactiveUser(request);
	}

	@PostMapping("/usersbystatus")
	public List<UserDto> getUserByStatus(@RequestBody GetUserByStatusDto request) {
		return adminService.getUserByStatus(request);
	}

	@PostMapping("/userbyaccountnumber")
	public User getUserByAccNum(@RequestBody GetUserByAccNumDto request) {
		return adminService.getUserByAccNum(request);
	}

	@PatchMapping("/updateuser")
	public ResponseEntity<String> updateUserDetails(@RequestBody User user) {
		String response = adminService.updateUserDetails(user);
		if (response.equals("User Details Saved Successfully")) {
			return ResponseEntity.ok(response);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}


}
