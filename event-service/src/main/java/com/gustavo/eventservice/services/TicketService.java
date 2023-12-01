package com.gustavo.eventservice.services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.gustavo.eventservice.dtos.TicketRequestDTO;
import com.gustavo.eventservice.dtos.TicketResponseDTO;

public interface TicketService {
	
	void insert(UUID eventId, TicketRequestDTO ticketRequestDto);
	
	Page<TicketResponseDTO> findByEvent(UUID eventId, Pageable pageable);
	
	Page<TicketResponseDTO> findByUser(UUID userId, Pageable pageable);

}
