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

import com.gustavo.eventservice.dtos.RegistrationRequestDTO;
import com.gustavo.eventservice.dtos.RegistrationResponseDTO;
import com.gustavo.eventservice.services.EventTicketService;

@RestController
@RequestMapping
public class EventUserController {
	
	@Autowired
	private EventTicketService eventTicketService;
	
	@PostMapping("/events/{eventId}/users/registration")
	public ResponseEntity<String> insert(@PathVariable UUID eventId, @Validated
            @RequestBody RegistrationRequestDTO registrationDto) {
		
		eventTicketService.insert(eventId, registrationDto);
		
		return ResponseEntity.status(HttpStatus.CREATED).body("Registration created successfully!"); 
	}
	
	@GetMapping("/events/{eventId}/users")
	public ResponseEntity<Page<RegistrationResponseDTO>> findByEvent(
			@PathVariable UUID eventId,
			@PageableDefault(page = 0, size = 10, sort = "ticketId", direction = Sort.Direction.ASC) Pageable pageable) {
		
		Page<RegistrationResponseDTO> registrationResponseDtoPage = eventTicketService.findByEvent(eventId, pageable);
		
		return ResponseEntity.status(HttpStatus.OK).body(registrationResponseDtoPage);
	}
	

}
