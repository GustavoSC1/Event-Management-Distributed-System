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
import com.gustavo.userservice.dtos.rabbitmqDtos.UserEventDTO;
import com.gustavo.userservice.entities.Role;
import com.gustavo.userservice.entities.User;
import com.gustavo.userservice.entities.enums.ActionType;
import com.gustavo.userservice.entities.enums.RoleType;
import com.gustavo.userservice.entities.enums.UserStatus;
import com.gustavo.userservice.producers.UserProducer;
import com.gustavo.userservice.repositories.UserRepository;
import com.gustavo.userservice.services.RoleService;
import com.gustavo.userservice.services.UserService;
import com.gustavo.userservice.services.exceptions.BusinessException;
import com.gustavo.userservice.services.exceptions.ObjectNotFoundException;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private UserProducer userProducer;
	
	@Override
	public UserResponseDTO insert(UserRequestDTO userRequestDto) {
		
		if(userRepository.existsByUsername(userRequestDto.getUsername())) {
			throw new BusinessException("Error: Username is already taken!");
		}
		
		if(userRepository.existsByEmail(userRequestDto.getEmail())) {
			throw new BusinessException("Error: Email is already taken!");
		}
		
		Role role = roleService.findByRoleName(RoleType.ROLE_USER);
		
		User user = new User();
		BeanUtils.copyProperties(userRequestDto, user);
		user.setUserStatus(UserStatus.ACTIVE);
		user.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
		user.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
		
		user.getRoles().add(role);
		
		userRepository.save(user);
		
		UserEventDTO userEventDto = new UserEventDTO(user);
		userEventDto.setActionType(ActionType.CREATE.toString());
		
		userProducer.produceUserEvent(userEventDto);
		
		UserResponseDTO userResponseDto = new UserResponseDTO();
		
		BeanUtils.copyProperties(user, userResponseDto);
		
		return userResponseDto;
	}
	
	@Override
	public UserResponseDTO getOneUser(UUID userId) {
		
		User user = findById(userId);
		
		UserResponseDTO userResponseDto = new UserResponseDTO();
		
		BeanUtils.copyProperties(user, userResponseDto);
		
		return userResponseDto;		
	}
	
	@Override
	public Page<UserResponseDTO> findAll(String name, String email, Pageable pageable) {
		
		Page<User> userPage = userRepository.findAllByNameAndEmail(name, email, pageable);
		
		Page<UserResponseDTO> urserResponseDtoPage = userPage.map(obj -> {
			UserResponseDTO userResponseDto = new UserResponseDTO();		
			BeanUtils.copyProperties(obj, userResponseDto);
			return userResponseDto;});
				
		return urserResponseDtoPage;		
	}
	
	@Override
	public UserResponseDTO update(UUID userId, UserRequestDTO userRequestDto) {
		
		User user = findById(userId);
	
		user.setFullName(userRequestDto.getFullName());
		user.setPhone(userRequestDto.getPhone());
		user.setCpf(userRequestDto.getCpf());
		user.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
		
		userRepository.save(user);
		
		UserEventDTO userEventDto = new UserEventDTO(user);
		userEventDto.setActionType(ActionType.UPDATE.toString());
		
		userProducer.produceUserEvent(userEventDto);
		
		UserResponseDTO userResponseDto = new UserResponseDTO();
		
		BeanUtils.copyProperties(user, userResponseDto);
		
		return userResponseDto;
	}
	
	@Override
	public void delete(UUID userId) {
				
		User user = findById(userId);
		
		userRepository.delete(user);	
		
		UserEventDTO userEventDto = new UserEventDTO(user);
		userEventDto.setActionType(ActionType.DELETE.toString());
		
		userProducer.produceUserEvent(userEventDto);
	}
	
	@Override
	public void updatePassword(UUID userId, UserRequestDTO userRequestDto) {

		User user = findById(userId);
		
		if(!user.getPassword().equals(userRequestDto.getOldPassword())) {
			throw new BusinessException("Error: Mismatched old password");
		} else {
			user.setPassword(userRequestDto.getPassword());
			user.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
			
			userRepository.save(user);
		}
	}
	
	@Override
	public UserResponseDTO updateImage(UUID userId, UserRequestDTO userRequestDto) {
		
		Optional<User> userOptional = userRepository.findById(userId);

		User user = userOptional.orElseThrow(() -> new ObjectNotFoundException("User not found! Id: " + userId));
	
		user.setImageUrl(userRequestDto.getImageUrl());
		user.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
		
		userRepository.save(user);
		
		UserEventDTO userEventDto = new UserEventDTO(user);
		userEventDto.setActionType(ActionType.UPDATE.toString());
		
		userProducer.produceUserEvent(userEventDto);
		
		UserResponseDTO userResponseDto = new UserResponseDTO();
		
		BeanUtils.copyProperties(user, userResponseDto);
		
		return userResponseDto;
	}
	
	@Override
	public User findById(UUID userId) {
		
		Optional<User> userOptional = userRepository.findById(userId);
		
		return userOptional.orElseThrow(() -> new ObjectNotFoundException("User not found! Id: " + userId));
	}

}
