package com.gustavo.userservice.services.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.gustavo.userservice.entities.User;
import com.gustavo.userservice.repositories.UserRepository;
import com.gustavo.userservice.services.CurrentUserService;
import com.gustavo.userservice.utils.UserAuthenticated;

@Service
public class CurrentUserServiceImpl implements CurrentUserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserAuthenticated getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		Optional<User> userOptional = userRepository.findByUsername(authentication.getName());
		
		User user = userOptional.orElseThrow(() -> new UsernameNotFoundException(authentication.getName()));
		
		return new UserAuthenticated(user.getUserId(), user.getUsername(), user.getPassword(), user.getUserStatus(), user.getRoles());
	}

}
