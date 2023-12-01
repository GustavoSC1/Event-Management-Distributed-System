package com.gustavo.eventservice.services.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.gustavo.eventservice.dtos.EventResponseDTO;
import com.gustavo.eventservice.dtos.TicketRequestDTO;
import com.gustavo.eventservice.dtos.TicketResponseDTO;
import com.gustavo.eventservice.dtos.UserResponseDTO;
import com.gustavo.eventservice.entities.Event;
import com.gustavo.eventservice.entities.Ticket;
import com.gustavo.eventservice.entities.User;
import com.gustavo.eventservice.entities.enums.EventStatus;
import com.gustavo.eventservice.repositories.TicketRepository;
import com.gustavo.eventservice.services.EventService;
import com.gustavo.eventservice.services.TicketService;
import com.gustavo.eventservice.services.UserService;
import com.gustavo.eventservice.services.exceptions.BusinessException;

@Service
public class TicketServiceImpl implements TicketService {
	
	@Autowired
	private TicketRepository ticketRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private EventService eventService;

	public void insert(UUID eventId, TicketRequestDTO ticketRequestDto) {
		
		Event event = eventService.findById(eventId);
		
		User user = userService.findById(ticketRequestDto.getUserId());
		
		if(!event.getEventStatus().equals(EventStatus.OPEN_TO_REGISTRATIONS) && 
		   !event.getEventStatus().equals(EventStatus.ONGOING)) {
			throw new BusinessException("It is not possible to register for this event!");
		}
		
		if(ticketRepository.existsByUserAndEvent(user, event)) {
			throw new BusinessException("Registration already exists!");
		}
		
		if(ticketRepository.countByEvent(event) >= event.getCapacity()) {
			throw new BusinessException("The event has reached maximum capacity!");
		}
				
		Ticket eventTicket = new Ticket();
		
		eventTicket.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));
		eventTicket.setEvent(event);
		eventTicket.setUser(user);
		
		if(event.getPrice().equals(0.0)) {
			eventTicket.setIsPaid(true);
		} else {
			eventTicket.setIsPaid(false);
		}
		
		ticketRepository.save(eventTicket);
	}	
	
	public Page<TicketResponseDTO> findByEvent(UUID eventId, Pageable pageable) {
		
		Event event = eventService.findById(eventId);
		
		Page<Ticket> eventTicket = ticketRepository.findByEvent(event, pageable);
		
		Page<TicketResponseDTO> ticketResponseDtoPage = eventTicket.map(obj -> {
			TicketResponseDTO ticketResponseDto = new TicketResponseDTO();
			UserResponseDTO userResponseDto = new UserResponseDTO();
			BeanUtils.copyProperties(obj, ticketResponseDto);
			BeanUtils.copyProperties(obj.getUser(), userResponseDto);
			ticketResponseDto.setUserResponseDto(userResponseDto);
			return ticketResponseDto;});
		
		return ticketResponseDtoPage;		
	}
	
	public Page<TicketResponseDTO> findByUser(UUID userId, Pageable pageable) {
		
		User user = userService.findById(userId);
		
		Page<Ticket> eventTicket = ticketRepository.findByUser(user, pageable);
		
		Page<TicketResponseDTO> ticketResponseDtoPage = eventTicket.map(obj -> {
			TicketResponseDTO ticketResponseDto = new TicketResponseDTO();
			EventResponseDTO eventResponseDto = new EventResponseDTO();
			BeanUtils.copyProperties(obj, ticketResponseDto);
			BeanUtils.copyProperties(obj.getEvent(), eventResponseDto);
			ticketResponseDto.setEventResponseDto(eventResponseDto);
			return ticketResponseDto;});
		
		return ticketResponseDtoPage;		
	}

}
