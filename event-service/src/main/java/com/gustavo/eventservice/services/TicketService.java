package com.gustavo.eventservice.services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.gustavo.eventservice.dtos.TicketRequestDTO;
import com.gustavo.eventservice.dtos.TicketResponseDTO;
import com.gustavo.eventservice.dtos.rabbitmqDtos.PaymentMadeEventDTO;
import com.gustavo.eventservice.entities.Ticket;

public interface TicketService {
	
	void insert(UUID eventId, TicketRequestDTO ticketRequestDto);
	
	void setTicketPaid(PaymentMadeEventDTO paymentMadeEventDto);
	
	Ticket findById(UUID ticketId);
	
	Page<TicketResponseDTO> findByEvent(UUID eventId, Pageable pageable);
	
	Page<TicketResponseDTO> findByUser(UUID userId, Pageable pageable);

}
