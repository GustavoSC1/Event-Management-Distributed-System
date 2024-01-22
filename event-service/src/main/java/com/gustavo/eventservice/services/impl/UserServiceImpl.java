package com.gustavo.eventservice.services.impl;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.gustavo.eventservice.dtos.UserResponseDTO;
import com.gustavo.eventservice.entities.User;
import com.gustavo.eventservice.repositories.UserRepository;
import com.gustavo.eventservice.services.UserService;
import com.gustavo.eventservice.services.exceptions.ObjectNotFoundException;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserResponseDTO insert(User user) {
		userRepository.save(user);
		
		UserResponseDTO userResponseDto = new UserResponseDTO();
		BeanUtils.copyProperties(user, userResponseDto);
		
		return userResponseDto;
	}
	
	@Override
	public User findById(UUID userId) {
		Optional<User> userOptional = userRepository.findById(userId);
		
		return userOptional.orElseThrow(() -> new ObjectNotFoundException("Error: User not found! Id: " + userId));
	}
	
	@Override
	public Page<UserResponseDTO> findStaffEvent(UUID eventId, Pageable pageable) {
		
		Page<User> userPage = userRepository.findAllByStaffEventsEventId(eventId, pageable);				
		
		return userPage.map(obj -> {
			UserResponseDTO userResponseDto = new UserResponseDTO();
			BeanUtils.copyProperties(obj, userResponseDto);
			return userResponseDto;});		
	}
		
}
