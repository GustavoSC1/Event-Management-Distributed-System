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

import com.gustavo.userservice.dtos.clientsDtos.TicketResponseDTO;
import com.gustavo.userservice.services.UserTicketService;

@RestController
public class UserTicketController {
	
	@Autowired
	private UserTicketService userTicketService;
	
	@GetMapping("/users/{userId}/tickets")
	public ResponseEntity<Page<TicketResponseDTO>> findAllTicketsByUser(
			@PathVariable UUID userId,
			@PageableDefault(page = 0, size = 10, sort = "ticketId", direction = Sort.Direction.ASC) Pageable pageable) {
		
		Page<TicketResponseDTO> ticketResponseDtoPage = userTicketService.findAllTicketsByUser(userId, pageable);

		return ResponseEntity.status(HttpStatus.OK).body(ticketResponseDtoPage);
	}

}
