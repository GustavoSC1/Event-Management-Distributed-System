package com.gustavo.userservice.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.gustavo.userservice.dtos.clientsDtos.NotificationResponseDTO;
import com.gustavo.userservice.services.UserNotificationService;

@RestController
public class UserNotificationController {
	
	@Autowired
	private UserNotificationService userNotificationService;
	
	@GetMapping("/users/{userId}/notifications")
	public ResponseEntity<Page<NotificationResponseDTO>> findAllNotificationsByUser(
			@PathVariable UUID userId,
			@PageableDefault(page = 0, size = 10, sort = "notificationId", direction = Sort.Direction.ASC) Pageable pageable) {
		
		Page<NotificationResponseDTO> notificationResponseDtoPage = 
				userNotificationService.findAllNotificationsByUser(userId, pageable);
		
		return ResponseEntity.status(HttpStatus.OK).body(notificationResponseDtoPage);
	}

}
