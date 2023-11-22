package com.gustavo.eventservice.services;

import java.util.UUID;

import com.gustavo.eventservice.dtos.EventRequestDTO;
import com.gustavo.eventservice.dtos.EventResponseDTO;

public interface EventService {
	
	EventResponseDTO insert(EventRequestDTO  eventRequestDto);
	
	EventResponseDTO getOneEvent(UUID eventId);

}
