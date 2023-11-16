package com.gustavo.userservice.services.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import com.gustavo.userservice.services.exceptions.BusinessException;
import com.gustavo.userservice.services.exceptions.ObjectNotFoundException;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	public UserResponseDTO insert(UserRequestDTO userRequestDto) {
		
		if(userRepository.existsByUsername(userRequestDto.getUsername())) {
			throw new BusinessException("Error: Username is already taken!");
		}
		
		if(userRepository.existsByEmail(userRequestDto.getEmail())) {
			throw new BusinessException("Error: Email is already taken!");
		}
		
		Role role = roleRepository.findByRoleName(RoleType.ROLE_USER)
				.orElseThrow(() -> new ObjectNotFoundException("Error: Role is not found!"));
		
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
		
		User user = userOptional.orElseThrow(() -> new ObjectNotFoundException("User not found! Id: " + userId));
		
		UserResponseDTO userResponseDto = new UserResponseDTO();
		
		BeanUtils.copyProperties(user, userResponseDto);
		
		return userResponseDto;		
	}
	
	public Page<UserResponseDTO> findAll(String name, String email, Pageable pageable) {
		
		Page<User> userPage = userRepository.findByNameLikeAndEmailLike(name, email, pageable);
		
		Page<UserResponseDTO> urserResponseDtoPage = userPage.map(obj -> {
			UserResponseDTO userResponseDto = new UserResponseDTO();		
			BeanUtils.copyProperties(obj, userResponseDto);
			return userResponseDto;});
		
		return urserResponseDtoPage;		
	}
	
	public UserResponseDTO update(UUID userId, UserRequestDTO userRequestDto) {
		
		Optional<User> userOptional = userRepository.findById(userId);

		User user = userOptional.orElseThrow(() -> new ObjectNotFoundException("User not found Id: " + userId));
	
		user.setFullName(userRequestDto.getFullName());
		user.setPhone(userRequestDto.getPhone());
		user.setCpf(userRequestDto.getCpf());
		user.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
		
		userRepository.save(user);
		
		UserResponseDTO userResponseDto = new UserResponseDTO();
		
		BeanUtils.copyProperties(user, userResponseDto);
		
		return userResponseDto;
	}
	
	public void delete(UUID userId) {
		
		Optional<User> userOptional = userRepository.findById(userId);
		
		User user = userOptional.orElseThrow(() -> new ObjectNotFoundException("User not found! Id: " + userId));
		
		userRepository.delete(user);		
	}
	
	public void updatePassword(UUID userId, UserRequestDTO userRequestDto) {
		Optional<User> userOptional = userRepository.findById(userId);

		User user = userOptional.orElseThrow(() -> new ObjectNotFoundException("User not found! Id: " + userId));
		
		if(!user.getPassword().equals(userRequestDto.getOldPassword())) {
			throw new BusinessException("Error: Mismatched old password");
		} else {
			user.setPassword(userRequestDto.getPassword());
			user.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
			
			userRepository.save(user);
		}
	}
	
	public UserResponseDTO updateImage(UUID userId, UserRequestDTO userRequestDto) {
		
		Optional<User> userOptional = userRepository.findById(userId);

		User user = userOptional.orElseThrow(() -> new ObjectNotFoundException("User not found! Id: " + userId));
	
		user.setImageUrl(userRequestDto.getImageUrl());
		user.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
		
		userRepository.save(user);
		
		UserResponseDTO userResponseDto = new UserResponseDTO();
		
		BeanUtils.copyProperties(user, userResponseDto);
		
		return userResponseDto;
	}

}
