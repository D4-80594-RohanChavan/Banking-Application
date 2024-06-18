package com.app.team2.technotribe.krasvbank.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.SignatureException;

import java.security.Key;
import java.util.Date;


@Component
public class JwtTokenProvider {

	@Value("${app.jwt-secret}")
    private String jwtSecret;

	
	@Value("${app.jwt-expiration}")
	private long JwtExpirationDate;
	
	public String generateToken(Authentication authentication) {
		String username=authentication.getName();
		Date currentDate=new Date();
		Date expireDate=new Date(currentDate.getTime()+JwtExpirationDate);
		
		return Jwts.builder()
				.setSubject(username)
				.setIssuedAt(currentDate)
				.setExpiration(expireDate)
				.signWith(key())
				.compact();
	}
	
	private Key key() {
		byte[] bytes=Decoders.BASE64.decode(jwtSecret);
		return Keys.hmacShaKeyFor(bytes);
	}
	
	public String getUserName(String token) {
		Claims claims=Jwts.parserBuilder()
				.setSigningKey(key())
						.build()
						.parseClaimsJws(token)
						.getBody();
				return claims.getSubject();
	}
	
	public boolean validateToken(String token) throws SignatureException {
		try {
			Jwts.parserBuilder()
			.setSigningKey(key())
			.build()
			.parse(token);
			return true;
		}catch(ExpiredJwtException | MalformedJwtException | IllegalArgumentException e) {
			throw new RuntimeException(e);
		}
	}
}
