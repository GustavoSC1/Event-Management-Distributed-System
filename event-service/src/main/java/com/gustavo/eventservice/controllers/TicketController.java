package com.gustavo.eventservice.controllers;

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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping
@Tag(name = "Ticket endpoint")
public class TicketController {
	
	Logger log = LogManager.getLogger(TicketController.class);
	
	@Autowired
	private TicketService ticketService;
	
	@Operation(summary = "Save a ticket")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Ticket successfully saved"),
			@ApiResponse(responseCode = "400", description = "This request can be processed"),
			@ApiResponse(responseCode = "403", description = "You are not allowed to make this request"),
			@ApiResponse(responseCode = "422", description = "Data validation error")
	})
	@PostMapping("/tickets/events/{eventId}/users")
	public ResponseEntity<String> insert(@PathVariable UUID eventId, @Validated
            @RequestBody TicketRequestDTO ticketRequestDto) {
		log.debug("POST ticketController insert eventId: {} ticketRequestDto received {}", eventId, ticketRequestDto.toString());
		ticketService.insert(eventId, ticketRequestDto);
		
		return ResponseEntity.status(HttpStatus.CREATED).body("Registration completed successfully!"); 
	}
	
	@Operation(summary = "Find all tickets by event")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Staff found successfully"),
			@ApiResponse(responseCode = "403", description = "You are not allowed to make this request")
	})
	@GetMapping("/tickets/events/{eventId}/users")
	public ResponseEntity<Page<TicketResponseDTO>> findByEvent(
			@PathVariable UUID eventId,
			@PageableDefault(page = 0, size = 10, sort = "ticketId", direction = Sort.Direction.ASC) Pageable pageable) {
		log.debug("GET ticketController findByEvent eventId: {} received", eventId);
		Page<TicketResponseDTO> ticketResponseDtoPage = ticketService.findByEvent(eventId, pageable);
		
		return ResponseEntity.status(HttpStatus.OK).body(ticketResponseDtoPage);
	}
	
	@Operation(summary = "Find all tickets by user")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Staff found successfully"),
			@ApiResponse(responseCode = "403", description = "You are not allowed to make this request")
	})
	@GetMapping("/tickets/users/{userId}/events")
	public ResponseEntity<Page<TicketResponseDTO>> findByUser(
			@PathVariable UUID userId,
			@PageableDefault(page = 0, size = 10, sort = "ticketId", direction = Sort.Direction.ASC) Pageable pageable) {
		log.debug("GET ticketController findByUser userId: {} received", userId);
		Page<TicketResponseDTO> ticketResponseDtoPage = ticketService.findByUser(userId, pageable);
		
		return ResponseEntity.status(HttpStatus.OK).body(ticketResponseDtoPage);
	}
	

}
