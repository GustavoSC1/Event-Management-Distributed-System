package com.gustavo.eventservice.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/events")
@Tag(name = "Event endpoint")
public class EventController {
	
	Logger log = LogManager.getLogger(EventController.class);
	
	@Autowired
	private EventService eventService;
	
	@Operation(summary = "Save a event")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Event successfully saved"),
			@ApiResponse(responseCode = "400", description = "This request can be processed"),
			@ApiResponse(responseCode = "403", description = "You are not allowed to make this request"),
			@ApiResponse(responseCode = "422", description = "Data validation error")
	})
	@PostMapping
	public ResponseEntity<EventResponseDTO> insert(@RequestBody @Validated(EventRequestDTO.EventView.EventPost.class) 
									EventRequestDTO eventRequestDto) {
		log.debug("POST eventController insert eventRequestDto received {}", eventRequestDto.toString());
		EventResponseDTO eventResponseDto =  eventService.insert(eventRequestDto);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(eventResponseDto);
	}
	
	@Operation(summary = "Obtains a event details by id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Event obtained successfully"),
			@ApiResponse(responseCode = "404", description = "Could not find the requested data")
	})
	@GetMapping("/{eventId}")
	public ResponseEntity<EventResponseDTO> getOneEvent(@PathVariable UUID eventId) {
		log.debug("GET eventController getOneEvent eventId: {} received", eventId);
		EventResponseDTO eventResponseDto = eventService.getOneEvent(eventId);
		
		return ResponseEntity.status(HttpStatus.OK).body(eventResponseDto);
	}
	
	@Operation(summary = "Find all Events")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Events found successfully")
	})
	@GetMapping
	public ResponseEntity<Page<EventResponseDTO>> getAllEvents(
			@RequestParam(value="search", defaultValue="") String search,
			@RequestParam(value="place", defaultValue="") String place,
			@RequestParam(value="minPrice", defaultValue="0.0") Double minPrice,
			@RequestParam(value="maxPrice", defaultValue="9999999.0") Double maxPrice,
			@RequestParam(value="eventDate", defaultValue="") String eventDate,
			@PageableDefault(page = 0, size = 10, sort = "eventId", direction = Sort.Direction.ASC) Pageable pageable) {
		
		LocalDateTime date = ("".equals(eventDate)) ? LocalDateTime.now(ZoneId.of("UTC")) : LocalDateTime.parse(eventDate);
		log.debug("GET eventController getAllEvents search: {} place: {} minPrice: {} maxPrice: {} eventDate: {} received", search, place, minPrice, maxPrice, date);
		Page<EventResponseDTO> eventResponseDtoPage = eventService.findAll(search.replace(" ", "%"), place.replace(" ", "%"), minPrice, maxPrice, date, pageable);
		
		if(!eventResponseDtoPage.isEmpty()) {
			
			for(EventResponseDTO eventResponseDto: eventResponseDtoPage.toList()) {
				eventResponseDto.add(linkTo(methodOn(EventController.class).getOneEvent(eventResponseDto.getEventId())).withSelfRel());
			}
			
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(eventResponseDtoPage);
	}
	
	@Operation(summary = "Updates a event")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successfully edited event"),
			@ApiResponse(responseCode = "403", description = "You are not allowed to make this request"),
			@ApiResponse(responseCode = "404", description = "Could not find the requested data"),
			@ApiResponse(responseCode = "422", description = "Data validation error")
	})
	@PutMapping("/{eventId}")
	public ResponseEntity<EventResponseDTO> update(@PathVariable UUID eventId, 
			@RequestBody @Validated(EventRequestDTO.EventView.EventPut.class) EventRequestDTO eventRequestDto) {
		log.debug("PUT eventController update eventId: {} eventRequestDto received {}", eventId, eventRequestDto.toString());
		EventResponseDTO eventResponseDto = eventService.update(eventId, eventRequestDto);
		
		return ResponseEntity.status(HttpStatus.OK).body(eventResponseDto);
	}
	
	@Operation(summary = "Close a event")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successfully closed event"),
			@ApiResponse(responseCode = "400", description = "This request can be processed"),
			@ApiResponse(responseCode = "403", description = "You are not allowed to make this request"),
			@ApiResponse(responseCode = "404", description = "Could not find the requested data")
	})
	@PatchMapping("/{eventId}/close")
	public ResponseEntity<String> closeRegistrations(@PathVariable UUID eventId) {
		log.debug("PATCH eventController closeRegistrations eventId: {} received", eventId);
		eventService.closeRegistrations(eventId);
		
		return ResponseEntity.status(HttpStatus.OK).body("Registrations closed successfully.");
	}
	
	@Operation(summary = "Cancel a event")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Event successfully canceled"),
			@ApiResponse(responseCode = "400", description = "This request can be processed"),
			@ApiResponse(responseCode = "403", description = "You are not allowed to make this request"),
			@ApiResponse(responseCode = "404", description = "Could not find the requested data")
	})
	@PatchMapping("/{eventId}/cancel")
	public ResponseEntity<String> cancelEvent(@PathVariable UUID eventId) {
		log.debug("PATCH eventController cancelEvent eventId: {} received", eventId);
		eventService.cancelEvent(eventId);
		
		return ResponseEntity.status(HttpStatus.OK).body("Event canceled successfully.");
	}

}
