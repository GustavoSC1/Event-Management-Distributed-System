package com.gustavo.eventservice.services.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gustavo.eventservice.dtos.EventRequestDTO;
import com.gustavo.eventservice.dtos.EventResponseDTO;
import com.gustavo.eventservice.entities.Event;
import com.gustavo.eventservice.entities.User;
import com.gustavo.eventservice.repositories.EventRepository;
import com.gustavo.eventservice.services.EventService;
import com.gustavo.eventservice.services.UserService;
import com.gustavo.eventservice.services.exceptions.BusinessException;

@Service
public class EventServiceImpl implements EventService {
	
	@Autowired
	private EventRepository eventRepository;
	
	@Autowired
	private UserService userService;	
	
	public EventResponseDTO insert(EventRequestDTO  eventRequestDto) {
		
		LocalDateTime currentDate = LocalDateTime.now(ZoneId.of("UTC"));
		
		if(eventRequestDto.getEndDateTime().isBefore(eventRequestDto.getStartDateTime())) {
			throw new BusinessException("The End Date and Time of the event cannot be earlier than the Start Date and Time!");
		}
		
		if(eventRequestDto.getRegistrationEndDate().isBefore(currentDate)) {
			throw new BusinessException("The Registration End Date cannot be earlier than the Registration Start Date!");
		}
		
		if(eventRequestDto.getEndDateTime().isBefore(eventRequestDto.getRegistrationEndDate())) {
			throw new BusinessException("The registration end date cannot be later than the event end date and time!");
		}
				
		User user = userService.findById(eventRequestDto.getCreationUser());
		
		Event event = new Event();
		BeanUtils.copyProperties(eventRequestDto, event);
		
		event.setCreationDate(currentDate);
		event.setLastUpdateDate(currentDate);
		event.setCreationUser(user);		
		
		eventRepository.save(event);
		
		EventResponseDTO eventResponseDto = new EventResponseDTO();
		
		BeanUtils.copyProperties(event, eventResponseDto);
		
		return eventResponseDto;		
	}

}
