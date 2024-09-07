package com.gustavo.notificationservice.controllers;

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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gustavo.notificationservice.dtos.NotificationResponseDTO;
import com.gustavo.notificationservice.services.NotificationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping
@Tag(name = "Notification endpoint")
public class NotificationController {
	
	Logger log = LogManager.getLogger(NotificationController.class);
	
	@Autowired
	private NotificationService notificationService;
	
	@Operation(summary = "Find notifications by user")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Notifications found successfully"),
			@ApiResponse(responseCode = "403", description = "You are not allowed to make this request")
	})
	@GetMapping("/notifications/users/{userId}")
	public ResponseEntity<Page<NotificationResponseDTO>> findByUser(
			@PathVariable(value = "userId") UUID userId,
            @PageableDefault(page = 0, size = 10, sort = "notificationId", direction = Sort.Direction.ASC)
            Pageable pageable) {
		log.debug("GET notificationController findByUser userId: {} received", userId);
		Page<NotificationResponseDTO> notificationResponseDtoPage = notificationService.findByUser(userId, pageable);
		
		return ResponseEntity.status(HttpStatus.OK).body(notificationResponseDtoPage);
	}
	
	@Operation(summary = "Mark notification as read")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successfully edited notification"),
			@ApiResponse(responseCode = "403", description = "You are not allowed to make this request"),
			@ApiResponse(responseCode = "404", description = "Could not find the requested data"),
			@ApiResponse(responseCode = "422", description = "Data validation error")
	})
	@PatchMapping("/notifications/{notificationId}/users/{userId}")
	public ResponseEntity<String> markAsRead(@PathVariable(value = "userId") UUID userId, 
			@PathVariable(value = "notificationId") UUID notificationId) {
		log.debug("PATCH notificationController markAsRead userId: {} notificationId: {} received", userId, notificationId);
		notificationService.markAsRead(userId, notificationId);
		
		return ResponseEntity.status(HttpStatus.OK).body("Notification marked as read successfully!");
	}

}
