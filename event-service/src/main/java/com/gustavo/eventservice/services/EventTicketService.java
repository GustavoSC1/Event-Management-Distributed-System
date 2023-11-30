package com.gustavo.eventservice.services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.gustavo.eventservice.dtos.RegistrationRequestDTO;
import com.gustavo.eventservice.dtos.RegistrationResponseDTO;

public interface EventTicketService {
	
	public void insert(UUID eventId, RegistrationRequestDTO registrationDto);
	
	Page<RegistrationResponseDTO> findByEvent(UUID eventId, Pageable pageable);

}
