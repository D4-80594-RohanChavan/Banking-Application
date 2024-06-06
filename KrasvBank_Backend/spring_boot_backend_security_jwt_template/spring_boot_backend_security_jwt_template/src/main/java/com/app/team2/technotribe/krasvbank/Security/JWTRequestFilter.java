package com.app.team2.technotribe.krasvbank.Security;

import java.io.IOException;
import com.app.team2.technotribe.krasvbank.jwt_utils.JwtUtils;
import com.app.team2.technotribe.krasvbank.repository.TokenBlacklistRepository;

import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;

@Component
//custom filter
public class JWTRequestFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtils utils;
	  @Autowired
	    private TokenBlacklistRepository tokenBlacklistRepository;

	  @Override
	    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
	            throws ServletException, IOException {
	        String authHeader = request.getHeader("Authorization");
	        if (authHeader != null && authHeader.startsWith("Bearer ")) {
	            String token = authHeader.substring(7);
	            if (!tokenBlacklistRepository.contains(token)) {
	                try {
	                    Claims claims = utils.validateJwtToken(token);
	                    String email = utils.getUserNameFromJwtToken(claims);
	                    List<GrantedAuthority> authorities = utils.getAuthoritiesFromClaims(claims);
	                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, null, authorities);
	                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	                    SecurityContextHolder.getContext().setAuthentication(authentication);
	                } catch (Exception e) {
	                    response.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid token, please login");
	                    return;
	                }
	            } else {
	                response.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid token, please login");
	                return;
	            }
	        }
	        filterChain.doFilter(request, response);
	
//	@Override
//	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//			throws ServletException, IOException {
//		// check for authorization hdr
//		String authHeadr = request.getHeader("Authorization");
//		if (authHeadr != null && authHeadr.startsWith("Bearer")) {
//			System.out.println("got bearer token");
//			String token = authHeadr.substring(7);
//			Claims claims = utils.validateJwtToken(token);
//			// extract subject from the token
//			String email = utils.getUserNameFromJwtToken(claims);
//			// extract authorities from the token
//			List<GrantedAuthority> authorities = utils.getAuthoritiesFromClaims(claims);
//			// wrap user details (username/email +granted authorities ) in the
//			// username pwd token
//			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, null,
//					authorities);
//			// save above auth object in the spring sec ctx
//			SecurityContextHolder.getContext().setAuthentication(authentication);
//
//		} else
//			System.out.println("req did not contain any bearer token");
//		filterChain.doFilter(request, response);// passing the control to the nexyt filter in the chain

	}

}