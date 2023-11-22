package com.gustavo.eventservice.services.impl;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gustavo.eventservice.entities.User;
import com.gustavo.eventservice.repositories.UserRepository;
import com.gustavo.eventservice.services.UserService;
import com.gustavo.eventservice.services.exceptions.ObjectNotFoundException;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	public User findById(UUID userId) {
		Optional<User> userOptional = userRepository.findById(userId);
		
		return userOptional.orElseThrow(() -> new ObjectNotFoundException("User not found! Id: " + userId));
	}

}
