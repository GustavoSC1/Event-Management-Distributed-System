package com.gustavo.userservice.services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.gustavo.userservice.dtos.UserRequestDTO;
import com.gustavo.userservice.dtos.UserResponseDTO;
import com.gustavo.userservice.entities.User;

public interface UserService {
	
	UserResponseDTO insert(UserRequestDTO userRequestDto);
	
	UserResponseDTO getOneUser(UUID userId);
	
	Page<UserResponseDTO> findAll(String name, String email, Pageable pageable);
	
	UserResponseDTO update(UUID userId, UserRequestDTO userRequestDto);
	
	void delete(UUID userId);
	
	void updatePassword(UUID userId, UserRequestDTO userRequestDto);
	
	UserResponseDTO updateImage(UUID userId, UserRequestDTO userRequestDto);
	
	User findById(UUID userId);

}
