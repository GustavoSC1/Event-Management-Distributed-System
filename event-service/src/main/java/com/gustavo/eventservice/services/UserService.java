package com.gustavo.eventservice.services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.gustavo.eventservice.dtos.UserResponseDTO;
import com.gustavo.eventservice.entities.User;

public interface UserService {
	
	UserResponseDTO insert(User user);
	
	User findById(UUID userId);
	
	Page<UserResponseDTO> findStaffEvent(UUID eventId, Pageable pageable);
	
}
