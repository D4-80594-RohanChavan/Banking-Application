package com.app.team2.technotribe.krasvbank.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.app.team2.technotribe.krasvbank.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService{

	private UserRepository userRepository;
	  @Override
	    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	        return userRepository.findByEmail(username)
	                .orElseThrow(() -> new UsernameNotFoundException(username + " not found"));
	    }

}
