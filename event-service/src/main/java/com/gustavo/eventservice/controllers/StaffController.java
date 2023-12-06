package com.gustavo.eventservice.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gustavo.eventservice.dtos.EventResponseDTO;
import com.gustavo.eventservice.dtos.StaffRequestDTO;
import com.gustavo.eventservice.dtos.UserResponseDTO;
import com.gustavo.eventservice.services.EventService;
import com.gustavo.eventservice.services.UserService;

@RestController
@RequestMapping
public class StaffController {
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/staffs/events/{eventId}/users")
	public ResponseEntity<String> insert(@PathVariable UUID eventId, @Validated
            @RequestBody StaffRequestDTO staffRequestDto) {
		eventService.insertStaff(eventId, staffRequestDto);
		
		return ResponseEntity.status(HttpStatus.CREATED).body("Staff successfully registered!"); 
	}
	
	@GetMapping("/staffs/events/{eventId}/users")
	public ResponseEntity<Page<UserResponseDTO>> findStaffEvent(@PathVariable UUID eventId,
			@PageableDefault(page = 0, size = 10, sort = "userId", direction = Sort.Direction.ASC) Pageable pageable) {
		Page<UserResponseDTO> userResponseDto = userService.findStaffEvent(eventId, pageable);
		
		return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
	}
	
	@GetMapping("/staffs/users/{userId}/events")
	public ResponseEntity<Page<EventResponseDTO>> findStaffUsers(@PathVariable UUID userId,
			@PageableDefault(page = 0, size = 10, sort = "eventId", direction = Sort.Direction.ASC) Pageable pageable) {
		Page<EventResponseDTO> eventResponseDto = eventService.findStaffUsers(userId, pageable);
		
		return ResponseEntity.status(HttpStatus.OK).body(eventResponseDto);
	}

}
