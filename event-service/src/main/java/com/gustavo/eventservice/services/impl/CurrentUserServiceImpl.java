package com.gustavo.eventservice.services.impl;

import java.util.Optional;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.gustavo.eventservice.entities.User;
import com.gustavo.eventservice.repositories.UserRepository;
import com.gustavo.eventservice.services.CurrentUserService;
import com.gustavo.eventservice.utils.UserAuthenticated;

@Service
public class CurrentUserServiceImpl implements CurrentUserService {
	
	Logger log = LogManager.getLogger(CurrentUserServiceImpl.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserAuthenticated getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userId = ""; 
		if(authentication.getPrincipal() instanceof Jwt authToken){
			userId = authToken.getClaimAsString("userId");
	    }
		
		Optional<User> userOptional = userRepository.findById(UUID.fromString(userId));
	
		User user = userOptional.orElseThrow(() -> {
			log.warn("Authenticated user not found! username: {}", authentication.getName());
			return new UsernameNotFoundException(authentication.getName());
			});
		
		log.debug("GET currentUserServiceImpl getCurrentUser username: {} found", authentication.getName());
	
		return new UserAuthenticated(user.getUserId(), user.getUserStatus(), authentication.getAuthorities());
	}

}
