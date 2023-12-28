package com.gustavo.userservice.services.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.gustavo.userservice.clients.NotificationClient;
import com.gustavo.userservice.dtos.clientsDtos.NotificationResponseDTO;
import com.gustavo.userservice.services.UserNotificationService;
import com.gustavo.userservice.services.UserService;

@Service
public class UserNotificationServiceImpl implements UserNotificationService {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private NotificationClient notificationClient;
	
	@Override
	public Page<NotificationResponseDTO> findAllNotificationsByUser(UUID userId, Pageable pageable) {
		
		userService.findById(userId);
		
		return notificationClient.findAllNotificationsByUser(userId, pageable);
	}

}
