package com.gustavo.eventservice.services;

import java.util.UUID;

import com.gustavo.eventservice.dtos.RegistrationRequestDTO;

public interface EventTicketService {
	
	public void insert(UUID eventId, RegistrationRequestDTO registrationDto);

}
