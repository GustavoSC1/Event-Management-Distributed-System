package com.gustavo.userservice.configs;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

@Configuration
public class FeignConfig {
	
	@Bean
	public RequestInterceptor requestInterceptor() {
		return new RequestInterceptor() {
			public void apply(RequestTemplate template) {
				template.header("Authorization", "Bearer " + getJWT());
			}
		};
	}
	
	private String getJWT() {
		String token = "";
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication.getPrincipal() instanceof Jwt authToken){			
			token = authToken.getTokenValue();			
	    }
		return token;
	}

}
