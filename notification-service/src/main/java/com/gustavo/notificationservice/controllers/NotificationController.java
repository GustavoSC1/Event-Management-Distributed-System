package com.gustavo.notificationservice.controllers;

import java.util.UUID;

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

@RestController
@RequestMapping
public class NotificationController {
	
	@Autowired
	private NotificationService notificationService;
	
	@GetMapping("/users/{userId}/notifications")
	public ResponseEntity<Page<NotificationResponseDTO>> findByUser(
			@PathVariable(value = "userId")
            UUID userId,
            @PageableDefault(page = 0, size = 10, sort = "notificationId", direction = Sort.Direction.ASC)
            Pageable pageable) {
		
		Page<NotificationResponseDTO> notificationResponseDtoPage = notificationService.findByUser(userId, pageable);
		
		return ResponseEntity.status(HttpStatus.OK).body(notificationResponseDtoPage);
	}
	
	@PatchMapping("/users/{userId}/notifications/{notificationId}")
	public ResponseEntity<String> markAsRead(@PathVariable(value = "userId") UUID userId, 
			@PathVariable(value = "notificationId") UUID notificationId) {
		notificationService.markAsRead(userId, notificationId);
		
		return ResponseEntity.status(HttpStatus.OK).body("Notification marked as read successfully!");
	}

}
