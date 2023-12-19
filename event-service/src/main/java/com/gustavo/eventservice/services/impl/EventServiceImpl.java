package com.gustavo.eventservice.services.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.gustavo.eventservice.dtos.EventRequestDTO;
import com.gustavo.eventservice.dtos.EventResponseDTO;
import com.gustavo.eventservice.dtos.StaffRequestDTO;
import com.gustavo.eventservice.dtos.rabbitmqDtos.NotificationEventDto;
import com.gustavo.eventservice.entities.Event;
import com.gustavo.eventservice.entities.User;
import com.gustavo.eventservice.entities.enums.EventStatus;
import com.gustavo.eventservice.producers.NotificationProducer;
import com.gustavo.eventservice.repositories.EventRepository;
import com.gustavo.eventservice.services.EventService;
import com.gustavo.eventservice.services.UserService;
import com.gustavo.eventservice.services.exceptions.BusinessException;
import com.gustavo.eventservice.services.exceptions.ObjectNotFoundException;

@Service
public class EventServiceImpl implements EventService {
	
	@Autowired
	private EventRepository eventRepository;
	
	@Autowired
	private UserService userService;	
	
	@Autowired
	private NotificationProducer notificationProducer;
	
	public EventResponseDTO insert(EventRequestDTO  eventRequestDto) {
		
		User user = userService.findById(eventRequestDto.getCreationUser());
		
		Event event = new Event();
		BeanUtils.copyProperties(eventRequestDto, event);
		
		event.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
		event.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));		
		event.setCreationUser(user);	
		
		checkDates(event, eventRequestDto);

		updateStatus(event);

		eventRepository.save(event);
		
		NotificationEventDto notificationCommandDto = new NotificationEventDto();
        notificationCommandDto.setTitle("Event created successfully");
        notificationCommandDto.setMessage("You have just created a new event called " + event.getName());
        notificationCommandDto.setUserId(user.getUserId());

        notificationProducer.produceNotificationEvent(notificationCommandDto);
				
		EventResponseDTO eventResponseDto = new EventResponseDTO();
		
		BeanUtils.copyProperties(event, eventResponseDto);
		
		return eventResponseDto;		
	}
	
	public Page<EventResponseDTO> findAll(String search, String place, Double minPrice, Double maxPrice, LocalDateTime date, Pageable pageable) {
		
		Page<Event> eventPage = eventRepository.findByParams(search, place, minPrice, maxPrice, date, pageable);
	
		Page<EventResponseDTO> eventResponseDtoPage = eventPage.map(obj -> {
			EventResponseDTO eventResponseDto = new EventResponseDTO();
			BeanUtils.copyProperties(obj, eventResponseDto);
			return eventResponseDto;});
		
		return eventResponseDtoPage;
	}
	
	public EventResponseDTO getOneEvent(UUID eventId) {
		
		Event event = findById(eventId);
		
		EventResponseDTO eventResponseDto = new EventResponseDTO();
		
		BeanUtils.copyProperties(event, eventResponseDto);
		
		return eventResponseDto;		
	}
	
	public EventResponseDTO update(UUID eventId, EventRequestDTO eventRequestDto) {
				
		Event event = findById(eventId);
		
		event.setName(eventRequestDto.getName());
		event.setDescription(eventRequestDto.getDescription());
		event.setImageUrl(eventRequestDto.getImageUrl());		
		event.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
				
		updateStatus(event);
				
		eventRepository.save(event);
		
		NotificationEventDto notificationCommandDto = new NotificationEventDto();
        notificationCommandDto.setTitle("Event updated successfully");
        notificationCommandDto.setMessage("You just updated the " + event.getName() + " event");
        notificationCommandDto.setUserId(event.getCreationUser().getUserId());

        notificationProducer.produceNotificationEvent(notificationCommandDto);
		
		EventResponseDTO eventResponseDto = new EventResponseDTO();
		
		BeanUtils.copyProperties(event, eventResponseDto);
		
		return eventResponseDto;
	}
	
	public void closeRegistrations(UUID eventId) {
		
		Event event = findById(eventId);
		
		if(!event.getEventStatus().equals(EventStatus.CLOSED_TO_REGISTRATIONS) &&
		   !event.getEventStatus().equals(EventStatus.CANCELED) &&
		   !event.getEventStatus().equals(EventStatus.PAST)) {
			
			event.setEventStatus(EventStatus.CLOSED_TO_REGISTRATIONS);
			
		} else {
			new BusinessException("It is not possible to close registrations for this event!");
		}
		
		eventRepository.save(event);
		
		NotificationEventDto notificationCommandDto = new NotificationEventDto();
        notificationCommandDto.setTitle("Registrations closed successfully");
        notificationCommandDto.setMessage("You have closed registrations for the " + event.getName() + " event");
        notificationCommandDto.setUserId(event.getCreationUser().getUserId());

        notificationProducer.produceNotificationEvent(notificationCommandDto);
	}
	
	public void cancelEvent(UUID eventId) {
		
		Event event = findById(eventId);
		
		if(!event.getEventStatus().equals(EventStatus.CANCELED) &&
		   !event.getEventStatus().equals(EventStatus.PAST)) {
			
			event.setEventStatus(EventStatus.CANCELED);
			
		} else {
			new BusinessException("It is not possible to cancel this event!");
		}
		
		eventRepository.save(event);
		
		NotificationEventDto notificationCommandDto = new NotificationEventDto();
        notificationCommandDto.setTitle("Event successfully canceled");
        notificationCommandDto.setMessage("You canceled the " + event.getName() + " event");
        notificationCommandDto.setUserId(event.getCreationUser().getUserId());

        notificationProducer.produceNotificationEvent(notificationCommandDto);
	}
	
	public void insertStaff(UUID eventId, StaffRequestDTO staffRequestDTO) {
		
		Event event = findById(eventId);
		
		User user = userService.findById(staffRequestDTO.getUserId());
		
		if(event.getEventStatus().equals(EventStatus.PAST) || 
		   event.getEventStatus().equals(EventStatus.CANCELED)) {
			throw new BusinessException("It is not possible to add new users to the staff!");
		}
		
		if(event.getStaffUsers().contains(user)) {
			throw new BusinessException("This user is already on staff!");
		}
		
		event.getStaffUsers().add(user);
				
		eventRepository.save(event);
	}
	
	public Page<EventResponseDTO> findStaffUsers(UUID userId, Pageable pageable) {
		
		Page<Event> eventPage = eventRepository.findAllByStaffUsersUserId(userId, pageable);
		
		return eventPage.map(obj -> {
			EventResponseDTO eventResponseDto = new EventResponseDTO();
			BeanUtils.copyProperties(obj, eventResponseDto);
			return eventResponseDto;}); 	
	}
			
	public Event findById(UUID eventId) {
		
		Optional<Event> eventOptional = eventRepository.findById(eventId);
		
		return eventOptional.orElseThrow(() -> new ObjectNotFoundException("Event not found! Id: " + eventId));
	}
	
	public void checkDates(Event event, EventRequestDTO eventRequestDto) {
				
		if(eventRequestDto.getStartDateTime().isBefore(eventRequestDto.getRegistrationStartDate())) {
			new BusinessException("An event cannot start before ticket sales begin!");
		}
				
		if(eventRequestDto.getEndDateTime().isBefore(eventRequestDto.getStartDateTime())) {
			throw new BusinessException("The end date and Time of the event cannot be earlier than the start d and time!");
		}
		
		if(eventRequestDto.getRegistrationEndDate().isBefore(eventRequestDto.getRegistrationStartDate())) {
			throw new BusinessException("The registration end date cannot be earlier than the registration start date!");
		}
		
		if(eventRequestDto.getEndDateTime().isBefore(eventRequestDto.getRegistrationEndDate())) {
			throw new BusinessException("The registration end date cannot be later than the event end date and time!");
		}
	}
	
	public void updateStatus(Event event) {
		LocalDateTime currentDate = LocalDateTime.now(ZoneId.of("UTC"));
				
		if(event.getStartDateTime().isBefore(currentDate) && currentDate.isBefore(event.getEndDateTime())) {
			event.setEventStatus(EventStatus.ONGOING);
		} else if(currentDate.isAfter(event.getRegistrationStartDate()) && currentDate.isBefore(event.getRegistrationEndDate())) {
			event.setEventStatus(EventStatus.OPEN_TO_REGISTRATIONS);
		} else if(currentDate.isAfter(event.getRegistrationEndDate()) && currentDate.isBefore(event.getEndDateTime())) {
			event.setEventStatus(EventStatus.CLOSED_TO_REGISTRATIONS);			
		} else if(currentDate.isBefore(event.getRegistrationStartDate())) {
			event.setEventStatus(EventStatus.CLOSED_TO_REGISTRATIONS);
		} else if(currentDate.isAfter(event.getEndDateTime())) {
			event.setEventStatus(EventStatus.PAST);	
		}
	}

}
