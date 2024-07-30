package com.app.team2.technotribe.krasvbank.config;

import java.io.IOException;
import java.security.SignatureException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.j2ee.J2eeBasedPreAuthenticatedWebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
@Component

@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtTokenProvider jwtTokenProvider;
	private UserDetailsService userDetailsService;
	
	  public JwtAuthenticationFilter() {
		this.jwtTokenProvider = new JwtTokenProvider();
	        // Default constructor
	    }
	  
	public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }
	
	 @Override
	    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain)
	            throws ServletException, IOException {
	        
	        String token = getTokenFromRequest(request);
	        try {
				if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
				    String username = jwtTokenProvider.getUserName(token);
				    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
				    UsernamePasswordAuthenticationToken authenticationToken = 
				            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				    
				    authenticationToken.setDetails(new J2eeBasedPreAuthenticatedWebAuthenticationDetailsSource().buildDetails(request));
				    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
				}
			} catch (UsernameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SignatureException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	        filterChain.doFilter(request, response);
	    }

	
	private String getTokenFromRequest(HttpServletRequest request) {
		String bearerToken=request.getHeader("Authorization");
		if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}
}
