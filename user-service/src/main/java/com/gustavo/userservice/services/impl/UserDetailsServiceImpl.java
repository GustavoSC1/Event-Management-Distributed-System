package com.gustavo.userservice.services.impl;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.gustavo.userservice.repositories.UserRepository;
import com.gustavo.userservice.utils.UserAuthenticated;
import com.gustavo.userservice.entities.User;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	Logger log = LogManager.getLogger(UserDetailsServiceImpl.class);
	
	private final UserRepository userRepository;
			
	public UserDetailsServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Optional<User> userOptional = userRepository.findByUsername(username);
		
		User user = userOptional.orElseThrow(() -> {
			log.warn("User not found! username: {}", username);
			return new UsernameNotFoundException(username);}
		);
		
		log.debug("GET userDetailsServiceImpl loadUserByUsername username: {} found", username);
				
		return new UserAuthenticated(user.getUserId(), user.getUsername(), user.getPassword(), user.getUserStatus(), user.getRoles());
	}

}
