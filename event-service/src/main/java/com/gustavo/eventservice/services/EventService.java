package com.gustavo.eventservice.services;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.gustavo.eventservice.dtos.EventRequestDTO;
import com.gustavo.eventservice.dtos.EventResponseDTO;
import com.gustavo.eventservice.dtos.StaffRequestDTO;
import com.gustavo.eventservice.entities.Event;

public interface EventService {
	
	EventResponseDTO insert(EventRequestDTO  eventRequestDto);
	
	Page<EventResponseDTO> findAll(String search, String place, Double minPrice, Double maxPrice, LocalDateTime date, Pageable pageable);
	
	EventResponseDTO getOneEvent(UUID eventId);
	
	EventResponseDTO update(UUID userId, UUID eventId, EventRequestDTO eventRequestDto);
	
	void closeRegistrations(UUID userId, UUID eventId);
	
	void cancelEvent(UUID userId, UUID eventId);
	
	Event findById(UUID eventId);
	
	void insertStaff(UUID userId, UUID eventId, StaffRequestDTO staffRequestDTO);
	
	Page<EventResponseDTO> findStaffUsers(UUID userId, Pageable pageable);

}
