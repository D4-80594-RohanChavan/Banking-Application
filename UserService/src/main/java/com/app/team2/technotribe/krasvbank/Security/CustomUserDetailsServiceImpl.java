package com.app.team2.technotribe.krasvbank.Security;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.app.team2.technotribe.krasvbank.entity.User;
import com.app.team2.technotribe.krasvbank.exception.UserInactiveException;
import com.app.team2.technotribe.krasvbank.repository.UserRepository;

@Service
@Transactional
public class CustomUserDetailsServiceImpl implements UserDetailsService {

	// dep user dao
	@Autowired
	private UserRepository userRepo;

	 @Override
	    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
	        User user = userRepo.findByEmail(email)
	                .orElseThrow(() -> new UsernameNotFoundException("Invalid Email !!!!!"));
	        
	        if ("INACTIVE".equalsIgnoreCase(user.getStatus())) {
	            throw new UserInactiveException("YOUR ACCOUNT IS UNDER VERIFICATION");
	        }
	        
	        return new CustomUserDetails(user);
	    }

}
