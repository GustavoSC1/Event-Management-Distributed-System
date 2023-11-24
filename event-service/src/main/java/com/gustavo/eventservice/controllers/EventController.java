package com.gustavo.eventservice.controllers;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gustavo.eventservice.dtos.EventRequestDTO;
import com.gustavo.eventservice.dtos.EventResponseDTO;
import com.gustavo.eventservice.services.EventService;

@RestController
@RequestMapping("/events")
public class EventController {
	
	@Autowired
	private EventService eventService;
	
	@PostMapping
	public ResponseEntity<EventResponseDTO> insert(@RequestBody @Validated(EventRequestDTO.EventView.EventPost.class) 
									EventRequestDTO eventRequestDto) {
		EventResponseDTO eventResponseDto =  eventService.insert(eventRequestDto);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(eventResponseDto);
	}
	
	@GetMapping("/{eventId}")
	public ResponseEntity<EventResponseDTO> getOneEvent(@PathVariable UUID eventId) {
		EventResponseDTO eventResponseDto = eventService.getOneEvent(eventId);
		
		return ResponseEntity.status(HttpStatus.OK).body(eventResponseDto);
	}
	
	@GetMapping
	public ResponseEntity<Page<EventResponseDTO>> getAllEvents(
			@RequestParam(value="search", defaultValue="") String search,
			@RequestParam(value="place", defaultValue="") String place,
			@RequestParam(value="minPrice", defaultValue="0.0") Double minPrice,
			@RequestParam(value="maxPrice", defaultValue="9999999.0") Double maxPrice,
			@RequestParam(value="eventDate", defaultValue="") String eventDate,
			@PageableDefault(page = 0, size = 10, sort = "eventId", direction = Sort.Direction.ASC) Pageable pageable) {
		
		LocalDateTime date = ("".equals(eventDate)) ? LocalDateTime.now(ZoneId.of("UTC")) : LocalDateTime.parse(eventDate);
		
		Page<EventResponseDTO> eventResponseDtoPage = eventService.findAll(search.replace(" ", "%"), place.replace(" ", "%"), minPrice, maxPrice, date, pageable);

		return ResponseEntity.status(HttpStatus.OK).body(eventResponseDtoPage);
	}
	
	@PutMapping("/{eventId}")
	public ResponseEntity<EventResponseDTO> update(@PathVariable UUID eventId, 
			@RequestBody @Validated(EventRequestDTO.EventView.EventPut.class) EventRequestDTO eventRequestDto) {
		EventResponseDTO eventResponseDto = eventService.update(eventId, eventRequestDto);
		
		return ResponseEntity.status(HttpStatus.OK).body(eventResponseDto);
	}

}
