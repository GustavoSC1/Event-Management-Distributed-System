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

import com.gustavo.eventservice.dtos.TicketRequestDTO;
import com.gustavo.eventservice.dtos.TicketResponseDTO;
import com.gustavo.eventservice.services.TicketService;

@RestController
@RequestMapping
public class TicketController {
	
	@Autowired
	private TicketService ticketService;
	
	@PostMapping("/tickets/events/{eventId}/users/registration")
	public ResponseEntity<String> insert(@PathVariable UUID eventId, @Validated
            @RequestBody TicketRequestDTO ticketRequestDto) {
		
		ticketService.insert(eventId, ticketRequestDto);
		
		return ResponseEntity.status(HttpStatus.CREATED).body("Registration created successfully!"); 
	}
	
	@GetMapping("/tickets/events/{eventId}/users")
	public ResponseEntity<Page<TicketResponseDTO>> findByEvent(
			@PathVariable UUID eventId,
			@PageableDefault(page = 0, size = 10, sort = "ticketId", direction = Sort.Direction.ASC) Pageable pageable) {
		
		Page<TicketResponseDTO> ticketResponseDtoPage = ticketService.findByEvent(eventId, pageable);
		
		return ResponseEntity.status(HttpStatus.OK).body(ticketResponseDtoPage);
	}
	
	@GetMapping("/tickets/users/{userId}/events")
	public ResponseEntity<Page<TicketResponseDTO>> findByUser(
			@PathVariable UUID userId,
			@PageableDefault(page = 0, size = 10, sort = "ticketId", direction = Sort.Direction.ASC) Pageable pageable) {
		
		Page<TicketResponseDTO> ticketResponseDtoPage = ticketService.findByUser(userId, pageable);
		
		return ResponseEntity.status(HttpStatus.OK).body(ticketResponseDtoPage);
	}
	

}
