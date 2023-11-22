package com.gustavo.eventservice.services;

import com.gustavo.eventservice.dtos.EventRequestDTO;
import com.gustavo.eventservice.dtos.EventResponseDTO;

public interface EventService {
	
	EventResponseDTO insert(EventRequestDTO  eventRequestDto);

}
