package com.gustavo.userservice.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.gustavo.userservice.clients.NotificationClient;
import com.gustavo.userservice.dtos.clientsDtos.NotificationResponseDTO;
import com.gustavo.userservice.services.CurrentUserService;
import com.gustavo.userservice.services.UserNotificationService;
import com.gustavo.userservice.services.UserService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class UserNotificationServiceImpl implements UserNotificationService {
	
	Logger log = LogManager.getLogger(UserNotificationServiceImpl.class);
	
	@Autowired
	private UserService userService;
		
	@Autowired
	private CurrentUserService currentUserService;
	
	@Autowired
	private NotificationClient notificationClient;
		
	@Override
	@CircuitBreaker(name = "notificationsByUserCB", fallbackMethod = "fallbackMethod")
	public Page<NotificationResponseDTO> findAllNotificationsByUser(UUID userId, Pageable pageable) {
		
		UUID userAuthenticatedId = currentUserService.getCurrentUser().getId();
		
		if(!userAuthenticatedId.equals(userId)) {
			log.warn("Access denied! userId: {}", userId);
			throw new AccessDeniedException("Error: Access denied!");
		}
		
		userService.findById(userId);
		
		log.debug("GET userNotificationServiceImpl findAllNotificationsByUser userId: {} found", userId);
        log.info("Notifications found successfully userId: {}", userId);
		
		return notificationClient.findAllNotificationsByUser(userId, pageable);
	}
	
	public Page<NotificationResponseDTO> fallbackMethod(UUID userId, Throwable throwable) {
		
		log.error("userNotificationServiceImpl fallbackMethod userId: {}, cause {}", userId, throwable.toString());
		
		List<NotificationResponseDTO> notificationList = new ArrayList<>();
		return new PageImpl<>(notificationList);
	}

}
