package com.gustavo.eventservice.services;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.gustavo.eventservice.dtos.EventRequestDTO;
import com.gustavo.eventservice.dtos.EventResponseDTO;
import com.gustavo.eventservice.entities.Event;

public interface EventService {
	
	EventResponseDTO insert(EventRequestDTO  eventRequestDto);
	
	Page<EventResponseDTO> findAll(String search, String place, Double minPrice, Double maxPrice, LocalDateTime date, Pageable pageable);
	
	EventResponseDTO getOneEvent(UUID eventId);
	
	EventResponseDTO update(UUID eventId, EventRequestDTO eventRequestDto);
	
	void closeRegistrations(UUID eventId);
	
	void cancelEvent(UUID eventId);
	
	Event findById(UUID eventId);

}
