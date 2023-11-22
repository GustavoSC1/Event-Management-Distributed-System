package com.gustavo.eventservice.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gustavo.eventservice.dtos.EventRequestDTO;
import com.gustavo.eventservice.dtos.EventResponseDTO;
import com.gustavo.eventservice.services.EventService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/events")
public class EventController {
	
	@Autowired
	private EventService eventService;
	
	@PostMapping
	public ResponseEntity<EventResponseDTO> insert(@Valid @RequestBody EventRequestDTO eventRequestDto) {
		EventResponseDTO eventResponseDto =  eventService.insert(eventRequestDto);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(eventResponseDto);
	}
	
	@GetMapping("/{eventId}")
	public ResponseEntity<EventResponseDTO> getOneEvent(@PathVariable UUID eventId) {
		EventResponseDTO eventResponseDto = eventService.getOneEvent(eventId);
		
		return ResponseEntity.status(HttpStatus.OK).body(eventResponseDto);
	}

}
