package com.gustavo.userservice.services;

import java.util.List;
import java.util.UUID;

import com.gustavo.userservice.dtos.UserRequestDTO;
import com.gustavo.userservice.dtos.UserResponseDTO;

public interface UserService {

	UserResponseDTO insert(UserRequestDTO userRequestDto);
	
	UserResponseDTO getOneUser(UUID userId);
	
	List<UserResponseDTO> findAll(String username, String firstName, String lastName, String email, int page, int pageSize);
	
	UserResponseDTO update(UUID userId, UserRequestDTO userRequestDto);
	
	void delete(UUID userId);
	
	void updatePassword(UUID userId);
	
	void forgotPassword(String userEmail);
	
	void updateEmail(UUID userId, UserRequestDTO userRequestDto);
	
	UserResponseDTO updateImage(UUID userId, UserRequestDTO userRequestDto);
				
}
