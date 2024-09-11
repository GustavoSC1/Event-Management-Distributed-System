package com.gustavo.userservice.controllers;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@SecurityRequirement(name = "Keycloak")
@Tag(name = "User Notification endpoint")
public class UserNotificationController {
	
	Logger log = LogManager.getLogger(UserNotificationController.class);
	
	@Autowired
	private UserNotificationService userNotificationService;
	
	@Operation(summary = "Find notifications by user")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Notifications found successfully"),
			@ApiResponse(responseCode = "403", description = "You are not allowed to make this request"),
	})
	@GetMapping("/users/{userId}/notifications")
	public ResponseEntity<Page<NotificationResponseDTO>> findAllNotificationsByUser(
			@PathVariable UUID userId,
			@PageableDefault(page = 0, size = 10, sort = "notificationId", direction = Sort.Direction.ASC) Pageable pageable) {		
		log.debug("GET userNotificationController findAllNotificationsByUser userId: {} received", userId);
		Page<NotificationResponseDTO> notificationResponseDtoPage = 
				userNotificationService.findAllNotificationsByUser(userId, pageable);
		
		return ResponseEntity.status(HttpStatus.OK).body(notificationResponseDtoPage);
	}

}
