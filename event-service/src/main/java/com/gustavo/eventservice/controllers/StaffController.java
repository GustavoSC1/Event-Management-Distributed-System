package com.gustavo.eventservice.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gustavo.eventservice.dtos.StaffRequestDTO;
import com.gustavo.eventservice.services.EventService;

@RestController
@RequestMapping
public class StaffController {
	
	@Autowired
	private EventService eventService;
	
	@PostMapping("/staffs/events/{eventId}/users")
	public ResponseEntity<String> insert(@PathVariable UUID eventId, @Validated
            @RequestBody StaffRequestDTO staffRequestDto) {
		eventService.insertStaff(eventId, staffRequestDto);
		
		return ResponseEntity.status(HttpStatus.CREATED).body("Staff successfully registered!"); 
	}

}
