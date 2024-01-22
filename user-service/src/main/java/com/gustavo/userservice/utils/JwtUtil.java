package com.gustavo.userservice.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.time.Instant;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.gustavo.userservice.entities.User;

@Service
public class JwtUtil {
	
	@Autowired
	public JwtEncoder encoder;
	
	@Value("${jwt.expiration}")
	private Long expiration;
		
	public String generateTokenFromUser(User user) {
						
		String scope = user
				.getRoles().stream()
				.map(role -> role.getRoleName().toString())
				.collect(Collectors.joining(" "));
		
		return generateToken(user.getUsername(), user.getUserId().toString(), scope);
	}
	
	public String generateTokenFromAuthentication(Authentication authentication) {
		
		String scope = authentication
				.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(" "));
		
		UserAuthenticated userAuthenticated = (UserAuthenticated) authentication.getPrincipal();
		
		return generateToken(authentication.getName(), userAuthenticated.getId().toString(), scope);
	}
	
	public String generateToken(String username, String userId, String scope) {
		
		Instant now = Instant.now();
		
		JwtClaimsSet claims = JwtClaimsSet.builder()
				.issuer("event-management-distributed-system")
				.issuedAt(now)
				.expiresAt(now.plusMillis(expiration))
				.subject(username)
				.claim("userId", userId)
				.claim("roles", scope)				
				.build();
		
		return encoder.encode(
				JwtEncoderParameters.from(claims))
				.getTokenValue();
	}

}
