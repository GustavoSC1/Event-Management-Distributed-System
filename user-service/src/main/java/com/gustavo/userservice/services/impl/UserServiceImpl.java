package com.gustavo.userservice.services.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gustavo.userservice.dtos.UserRequestDTO;
import com.gustavo.userservice.dtos.UserResponseDTO;
import com.gustavo.userservice.entities.Role;
import com.gustavo.userservice.entities.User;
import com.gustavo.userservice.entities.enums.RoleType;
import com.gustavo.userservice.entities.enums.UserStatus;
import com.gustavo.userservice.repositories.RoleRepository;
import com.gustavo.userservice.repositories.UserRepository;
import com.gustavo.userservice.services.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	public UserResponseDTO insert(UserRequestDTO userRequestDto) {
		
		if(userRepository.existsByUsername(userRequestDto.getUsername())) {
			throw new RuntimeException("Error: Username is already taken!");
		}
		
		if(userRepository.existsByEmail(userRequestDto.getEmail())) {
			throw new RuntimeException("Error: Email is already taken!");
		}
		
		Role role = roleRepository.findByRoleName(RoleType.ROLE_USER)
				.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
		
		User user = new User();
		BeanUtils.copyProperties(userRequestDto, user);
		user.setUserStatus(UserStatus.ACTIVE);
		user.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
		user.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
		
		user.getRoles().add(role);
		
		userRepository.save(user);
		
		UserResponseDTO userResponseDto = new UserResponseDTO();
		
		BeanUtils.copyProperties(user, userResponseDto);
		
		return userResponseDto;
	}
	
	public UserResponseDTO getOneUser(UUID userId) {
		
		Optional<User> userOptional = userRepository.findById(userId);
		
		User user = userOptional.orElseThrow(() -> new RuntimeException("User not found."));
		
		UserResponseDTO userResponseDto = new UserResponseDTO();
		
		BeanUtils.copyProperties(user, userResponseDto);
		
		return userResponseDto;		
	}
	
	public UserResponseDTO update(UUID userId, UserRequestDTO userRequestDto) {
		
		Optional<User> userOptional = userRepository.findById(userId);

		User user = userOptional.orElseThrow(() -> new RuntimeException("User not found."));
	
		user.setFullName(userRequestDto.getFullName());
		user.setPhone(userRequestDto.getPhone());
		user.setCpf(userRequestDto.getCpf());
		user.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
		
		userRepository.save(user);
		
		UserResponseDTO userResponseDto = new UserResponseDTO();
		
		BeanUtils.copyProperties(user, userResponseDto);
		
		return userResponseDto;
	}

}
