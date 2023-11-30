package com.gustavo.eventservice.services.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.gustavo.eventservice.dtos.RegistrationRequestDTO;
import com.gustavo.eventservice.dtos.RegistrationResponseDTO;
import com.gustavo.eventservice.dtos.UserResponseDTO;
import com.gustavo.eventservice.entities.Event;
import com.gustavo.eventservice.entities.EventTicket;
import com.gustavo.eventservice.entities.User;
import com.gustavo.eventservice.entities.enums.EventStatus;
import com.gustavo.eventservice.repositories.EventTicketRepository;
import com.gustavo.eventservice.services.EventService;
import com.gustavo.eventservice.services.EventTicketService;
import com.gustavo.eventservice.services.UserService;
import com.gustavo.eventservice.services.exceptions.BusinessException;

@Service
public class EventTicketServiceImpl implements EventTicketService {
	
	@Autowired
	private EventTicketRepository eventTicketRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private EventService eventService;

	public void insert(UUID eventId, RegistrationRequestDTO registrationDto) {
		
		Event event = eventService.findById(eventId);
		
		User user = userService.findById(registrationDto.getUserId());
		
		if(!event.getEventStatus().equals(EventStatus.OPEN_TO_REGISTRATIONS) && 
		   !event.getEventStatus().equals(EventStatus.ONGOING)) {
			throw new BusinessException("It is not possible to register for this event!");
		}
		
		if(eventTicketRepository.existsByUserAndEvent(user, event)) {
			throw new BusinessException("Registration already exists!");
		}
		
		if(eventTicketRepository.countByEvent(event) >= event.getCapacity()) {
			throw new BusinessException("The event has reached maximum capacity!");
		}
				
		EventTicket eventTicket = new EventTicket();
		
		eventTicket.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));
		eventTicket.setEvent(event);
		eventTicket.setUser(user);
		
		if(event.getPrice().equals(0.0)) {
			eventTicket.setIsPaid(true);
		} else {
			eventTicket.setIsPaid(false);
		}
		
		eventTicketRepository.save(eventTicket);
	}	
	
	public Page<RegistrationResponseDTO> findByEvent(UUID eventId, Pageable pageable) {
		
		Event event = eventService.findById(eventId);
		
		Page<EventTicket> eventTicket = eventTicketRepository.findByEvent(event, pageable);
		
		Page<RegistrationResponseDTO> registrationResponseDtoPage = eventTicket.map(obj -> {
			RegistrationResponseDTO registrationResponseDto = new RegistrationResponseDTO();
			UserResponseDTO userResponseDto = new UserResponseDTO();
			BeanUtils.copyProperties(obj, registrationResponseDto);
			BeanUtils.copyProperties(obj.getUser(), userResponseDto);
			registrationResponseDto.setUserResponseDto(userResponseDto);
			return registrationResponseDto;});
		
		return registrationResponseDtoPage;		
	}

}
