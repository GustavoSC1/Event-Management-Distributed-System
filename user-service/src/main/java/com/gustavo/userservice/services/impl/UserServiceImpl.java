package com.gustavo.userservice.services.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gustavo.userservice.dtos.LoginResponseDTO;
import com.gustavo.userservice.dtos.UserRequestDTO;
import com.gustavo.userservice.dtos.UserResponseDTO;
import com.gustavo.userservice.dtos.rabbitmqDtos.UserEventDTO;
import com.gustavo.userservice.entities.RefreshToken;
import com.gustavo.userservice.entities.Role;
import com.gustavo.userservice.entities.User;
import com.gustavo.userservice.entities.enums.ActionType;
import com.gustavo.userservice.entities.enums.RoleType;
import com.gustavo.userservice.entities.enums.UserStatus;
import com.gustavo.userservice.producers.UserProducer;
import com.gustavo.userservice.repositories.UserRepository;
import com.gustavo.userservice.services.CurrentUserService;
import com.gustavo.userservice.services.RefreshTokenService;
import com.gustavo.userservice.services.RoleService;
import com.gustavo.userservice.services.UserService;
import com.gustavo.userservice.services.exceptions.BusinessException;
import com.gustavo.userservice.services.exceptions.ObjectNotFoundException;
import com.gustavo.userservice.utils.JwtUtil;
import com.gustavo.userservice.utils.UserAuthenticated;

@Service
public class UserServiceImpl implements UserService {
	
	Logger log = LogManager.getLogger(UserServiceImpl.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private CurrentUserService currentUserService;
	
	@Autowired
	private RefreshTokenService refreshTokenService;
	
	@Autowired
	private UserProducer userProducer;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
		
	@Autowired
	private JwtUtil jwtUtil;	
		
	@Override
	public LoginResponseDTO login(Authentication authentication) {	
		
		String token = jwtUtil.generateTokenFromAuthentication(authentication);
		
		List<String> roles = authentication.getAuthorities().stream().map(item -> item.getAuthority())
		        .collect(Collectors.toList());
		
		UserAuthenticated userAuthenticated = currentUserService.getCurrentUser();
		
		RefreshToken refreshToken = refreshTokenService.createRefreshToken(userAuthenticated.getId());
		
		log.debug("POST userServiceImpl login userId: {} logged", userAuthenticated.getId());
        log.info("User successfully logged userId: {}", userAuthenticated.getId());
		
		return new LoginResponseDTO(token, refreshToken.getToken(), userAuthenticated.getId(), userAuthenticated.getUsername(), roles);
	}
		
	@Override
	public UserResponseDTO insert(UserRequestDTO userRequestDto) {
		
		if(userRepository.existsByUsername(userRequestDto.getUsername())) {
			log.warn("Username {} is Already Taken!", userRequestDto.getUsername());
			throw new BusinessException("Error: Username is already taken!");
		}
		
		if(userRepository.existsByEmail(userRequestDto.getEmail())) {
			log.warn("Email {} is Already Taken!", userRequestDto.getEmail());
			throw new BusinessException("Error: Email is already taken!");
		}
		
		Role role = roleService.findByRoleName(RoleType.ROLE_USER);
		
		userRequestDto.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
		
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
		
		log.debug("POST userServiceImpl insert userId: {} saved", userResponseDto.getUserId());
        log.info("User saved successfully userId: {}", userResponseDto.getUserId());
        
		return userResponseDto;
	}
	
	@Override
	public UserResponseDTO getOneUser(UUID userId) {
		
		User user = findById(userId);
		
		UserResponseDTO userResponseDto = new UserResponseDTO();
		
		BeanUtils.copyProperties(user, userResponseDto);
		
		log.debug("GET userServiceImpl getOneUser userId: {} found", userResponseDto.getUserId());
        log.info("User found successfully userId: {}", userResponseDto.getUserId());
		
		return userResponseDto;		
	}
	
	@Override
	public Page<UserResponseDTO> findAll(String name, String email, Pageable pageable) {
		
		Page<User> userPage = userRepository.findAllByNameAndEmail(name, email, pageable);
		
		Page<UserResponseDTO> urserResponseDtoPage = userPage.map(obj -> {
			UserResponseDTO userResponseDto = new UserResponseDTO();		
			BeanUtils.copyProperties(obj, userResponseDto);
			return userResponseDto;});
		
		log.debug("GET userServiceImpl findAll name: {} email: {} found", name, email);
        log.info("Users found successfully name: {} email: {}", name, email);
        
		return urserResponseDtoPage;		
	}
	
	@Override
	public UserResponseDTO update(UUID userId, UserRequestDTO userRequestDto) {
		
		UUID userAuthenticatedId = currentUserService.getCurrentUser().getId();
		
		if(!userAuthenticatedId.equals(userId)) {
			log.warn("Access denied! userId: {}", userId);
			throw new AccessDeniedException("Error: Access denied!");
		}
		
		User user = findById(userId);
	
		user.setName(userRequestDto.getName());
		user.setPhone(userRequestDto.getPhone());
		user.setCpf(userRequestDto.getCpf());
		user.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
		
		userRepository.save(user);
		
		UserEventDTO userEventDto = new UserEventDTO(user);
		userEventDto.setActionType(ActionType.UPDATE.toString());
		
		userProducer.produceUserEvent(userEventDto);
		
		UserResponseDTO userResponseDto = new UserResponseDTO();
		
		BeanUtils.copyProperties(user, userResponseDto);
		
		log.debug("PUT userServiceImpl update userId: {} updated", userResponseDto.getUserId());
        log.info("User updated successfully userId: {}", userResponseDto.getUserId());
		
		return userResponseDto;
	}
	
	@Override
	public void delete(UUID userId) {
		
		UserAuthenticated userAuthenticated = currentUserService.getCurrentUser();
		
		if(!userAuthenticated.getId().equals(userId) && !userAuthenticated.hasRole(RoleType.ROLE_ADMIN)) {
			log.warn("Access denied! userId: {}", userId);
			throw new AccessDeniedException("Error: Access denied!");
		}
				
		User user = findById(userId);
		
		userRepository.delete(user);	
		
		UserEventDTO userEventDto = new UserEventDTO(user);
		userEventDto.setActionType(ActionType.DELETE.toString());
						
		userProducer.produceUserEvent(userEventDto);
		
		log.debug("DELETE userServiceImpl delete userId: {} deleted", userId);
        log.info("User successfully deleted userId: {}", userId);
	}
	
	@Override
	public void updatePassword(UUID userId, UserRequestDTO userRequestDto) {
		
		UUID userAuthenticatedId = currentUserService.getCurrentUser().getId();
		
		if(!userAuthenticatedId.equals(userId)) {
			log.warn("Access denied! userId: {}", userId);
			throw new AccessDeniedException("Error: Access denied!");
		}

		User user = findById(userId);
		
		if(!user.getPassword().equals(userRequestDto.getOldPassword())) {
			log.warn("Mismatched old password! userId: {}", userId);
			throw new BusinessException("Error: Mismatched old password");
		} else {
			user.setPassword(userRequestDto.getPassword());
			user.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
			
			userRepository.save(user);
			
			log.debug("PATCH userServiceImpl updatePassword userId: {} updated", user.getUserId());
	        log.info("User password updated successfully userId: {}", user.getUserId());
		}
	}
	
	@Override
	public UserResponseDTO updateImage(UUID userId, UserRequestDTO userRequestDto) {
		
		UUID userAuthenticatedId = currentUserService.getCurrentUser().getId();
		
		if(!userAuthenticatedId.equals(userId)) {
			log.warn("Access denied! userId: {}", userId);
			throw new AccessDeniedException("Error: Access denied!");
		}
		
		User user = findById(userId);
	
		user.setImageUrl(userRequestDto.getImageUrl());
		user.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
		
		userRepository.save(user);
		
		UserEventDTO userEventDto = new UserEventDTO(user);
		userEventDto.setActionType(ActionType.UPDATE.toString());
		
		userProducer.produceUserEvent(userEventDto);
		
		UserResponseDTO userResponseDto = new UserResponseDTO();
		
		BeanUtils.copyProperties(user, userResponseDto);
		
		log.debug("PATCH userServiceImpl updateImage userId: {} updated", user.getUserId());
        log.info("User image updated successfully userId: {}", user.getUserId());
		
		return userResponseDto;
	}
	
	@Override
	public User findById(UUID userId) {
		
		Optional<User> userOptional = userRepository.findById(userId);
		
		User user = userOptional.orElseThrow(() -> {
			log.warn("User not found! userId: {}", userId);
			return new ObjectNotFoundException("Error: User not found! Id: " + userId);
			});
		
		log.debug("GET userServiceImpl findById userId: {} found", userId);
		
		return user;
	}
	
}
