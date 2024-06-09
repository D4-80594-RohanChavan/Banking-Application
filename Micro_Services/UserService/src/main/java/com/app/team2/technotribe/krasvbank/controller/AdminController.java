package com.app.team2.technotribe.krasvbank.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.team2.technotribe.krasvbank.dto.GetUserByAccNumDto;
import com.app.team2.technotribe.krasvbank.dto.GetUserByStatusDto;
import com.app.team2.technotribe.krasvbank.dto.UpdateAccountStatusRequest;
import com.app.team2.technotribe.krasvbank.entity.User;
import com.app.team2.technotribe.krasvbank.service.impl.AdminService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

	@Autowired
	AdminService adminService;
	
	@PostMapping("/activateuser")
	@Operation(summary =  "Api to update the bank account status")
	public String activateuser(@RequestBody UpdateAccountStatusRequest request) {
		return adminService.activateUser(request);
	}
	@PostMapping("/inactiveUser")
	@Operation(summary =  "Api to update the bank account status")
	public String deactivateuser(@RequestBody UpdateAccountStatusRequest request) {
		return adminService.inactiveUser(request);
	}
	
	@PostMapping("/usersbystatus")
	public List<User> getUserByStatus(@RequestBody GetUserByStatusDto request){
		return adminService.getUserByStatus(request);
	}
	
	@PostMapping("/userbyaccountnumber")
	public User getUserByAccNum(@RequestBody GetUserByAccNumDto request){
		return adminService.getUserByAccNum(request);
	}
	
	@PutMapping("/updateuser")
	public String updateUserDetails(@RequestBody User user) {
		return adminService.updateUserDetails(user);
	}
	
	
}
