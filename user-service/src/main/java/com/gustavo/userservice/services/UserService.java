package com.gustavo.userservice.services;

import com.gustavo.userservice.dtos.UserRequestDTO;
import com.gustavo.userservice.dtos.UserResponseDTO;

public interface UserService {
	
	UserResponseDTO insert(UserRequestDTO userRequestDto);

}
