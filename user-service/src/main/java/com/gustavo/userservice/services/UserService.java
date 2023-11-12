package com.gustavo.userservice.services;

import java.util.UUID;

import com.gustavo.userservice.dtos.UserRequestDTO;
import com.gustavo.userservice.dtos.UserResponseDTO;

public interface UserService {
	
	UserResponseDTO insert(UserRequestDTO userRequestDto);
	
	UserResponseDTO getOneUser(UUID userId);
	
	UserResponseDTO update(UUID userId, UserRequestDTO userRequestDto);

}
