package com.gustavo.eventservice.services.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.gustavo.eventservice.dtos.EventRequestDTO;
import com.gustavo.eventservice.dtos.EventResponseDTO;
import com.gustavo.eventservice.dtos.StaffRequestDTO;
import com.gustavo.eventservice.dtos.rabbitmqDtos.NotificationEventDTO;
import com.gustavo.eventservice.entities.Event;
import com.gustavo.eventservice.entities.Ticket;
import com.gustavo.eventservice.entities.User;
import com.gustavo.eventservice.entities.enums.EventStatus;
import com.gustavo.eventservice.producers.NotificationProducer;
import com.gustavo.eventservice.repositories.EventRepository;
import com.gustavo.eventservice.repositories.TicketRepository;
import com.gustavo.eventservice.services.EventService;
import com.gustavo.eventservice.services.UserService;
import com.gustavo.eventservice.services.exceptions.BusinessException;
import com.gustavo.eventservice.services.exceptions.ObjectNotFoundException;

@Service
public class EventServiceImpl implements EventService {
	
	Logger log = LogManager.getLogger(EventServiceImpl.class);
	
	@Autowired
	private EventRepository eventRepository;
	
	@Autowired
	private TicketRepository ticketRepository;
			
	@Autowired
	private UserService userService;
		
	@Autowired
	private NotificationProducer notificationProducer;
	
	@Override
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
		
		NotificationEventDTO notificationEventDto = new NotificationEventDTO();
		notificationEventDto.setTitle("Event created successfully");
        notificationEventDto.setMessage("You have just created a new event called " + event.getName() + ".");
        notificationEventDto.setUserId(user.getUserId());

        notificationProducer.produceNotificationEvent(notificationEventDto);
				
		EventResponseDTO eventResponseDto = new EventResponseDTO();
		
		BeanUtils.copyProperties(event, eventResponseDto);
		
		log.debug("POST eventServiceImpl insert eventId: {} saved", eventResponseDto.getEventId());
        log.info("Event saved successfully eventId: {}", eventResponseDto.getEventId());
		
		return eventResponseDto;		
	}
	
	@Override
	public Page<EventResponseDTO> findAll(String search, String place, Double minPrice, Double maxPrice, LocalDateTime date, Pageable pageable) {
		
		Page<Event> eventPage = eventRepository.findByParams(search, place, minPrice, maxPrice, date, pageable);
	
		Page<EventResponseDTO> eventResponseDtoPage = eventPage.map(obj -> {
			EventResponseDTO eventResponseDto = new EventResponseDTO();
			BeanUtils.copyProperties(obj, eventResponseDto);
			return eventResponseDto;});
		
		log.debug("GET eventServiceImpl findAll search: {} place: {} minPrice: {} maxPrice: {} date: {} found", search, place, minPrice, maxPrice, date);
        log.info("Events found successfully search: {} place: {} minPrice: {} maxPrice: {} date: {}", search, place, minPrice, maxPrice, date);
		
		return eventResponseDtoPage;
	}
	
	@Override
	public EventResponseDTO getOneEvent(UUID eventId) {
		
		Event event = findById(eventId);
		
		updateStatus(event);
		
		eventRepository.save(event);
		
		EventResponseDTO eventResponseDto = new EventResponseDTO();
		
		BeanUtils.copyProperties(event, eventResponseDto);
		
		log.debug("GET eventServiceImpl getOneEvent eventId: {} found", eventResponseDto.getEventId());
        log.info("Event found successfully eventId: {}", eventResponseDto.getEventId());
		
		return eventResponseDto;		
	}
	
	@Override
	public EventResponseDTO update(UUID userId, UUID eventId, EventRequestDTO eventRequestDto) {
								
		Event event = findById(eventId);
		
		User user = userService.findById(userId);
		
		if(!event.getCreationUser().getUserId().equals(userId) && 
			!event.getStaffUsers().contains(user)) {
			log.warn("Access denied! userAuthenticatedId: {}", userId);
			throw new AccessDeniedException("Error: Access denied!");
		}

		event.setName(eventRequestDto.getName());
		event.setDescription(eventRequestDto.getDescription());
		event.setImageUrl(eventRequestDto.getImageUrl());		
		event.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
				
		updateStatus(event);
				
		eventRepository.save(event);
		
		NotificationEventDTO notificationEventDto = new NotificationEventDTO();
		
		notificationEventDto.setTitle("Updated event.");
        notificationEventDto.setMessage("The " + event.getName() + " event has been updated.");
        
		for(Ticket eventTicket: event.getTickets()) {
    		User userTicket = eventTicket.getUser();
    		notificationEventDto.setUserId(userTicket.getUserId());
    		notificationProducer.produceNotificationEvent(notificationEventDto);
    	}
		
		for(User userStaff: event.getStaffUsers()) {
    		notificationEventDto.setUserId(userStaff.getUserId());
    		notificationProducer.produceNotificationEvent(notificationEventDto);
    	}
		
        notificationEventDto.setTitle("Event updated successfully");
        notificationEventDto.setMessage("You just updated the " + event.getName() + " event.");
        notificationEventDto.setUserId(event.getCreationUser().getUserId());

        notificationProducer.produceNotificationEvent(notificationEventDto);
		
		EventResponseDTO eventResponseDto = new EventResponseDTO();
		
		BeanUtils.copyProperties(event, eventResponseDto);
		
		log.debug("PUT eventServiceImpl update eventId: {} updated", eventResponseDto.getEventId());
        log.info("Event updated successfully eventId: {}", eventResponseDto.getEventId());
		
		return eventResponseDto;
	}
	
	@Override
	public void closeRegistrations(UUID userId, UUID eventId) {
						
		Event event = findById(eventId);
		
		if(!event.getCreationUser().getUserId().equals(userId)) {
			log.warn("Access denied! userAuthenticatedId: {}", userId);
			throw new AccessDeniedException("Error: Access denied!");
		}

		if(!event.getEventStatus().equals(EventStatus.CLOSED_TO_REGISTRATIONS) &&
		   !event.getEventStatus().equals(EventStatus.CANCELED) &&
		   !event.getEventStatus().equals(EventStatus.PAST)) {
			
			event.setEventStatus(EventStatus.CLOSED_TO_REGISTRATIONS);
			
		} else {
			log.warn("It is not possible to close registrations for this event eventId: {}", eventId);
			new BusinessException("Error: It is not possible to close registrations for this event!");
		}
				
		eventRepository.save(event);
		
		NotificationEventDTO notificationEventDto = new NotificationEventDTO();
        notificationEventDto.setTitle("Registrations closed successfully.");
        notificationEventDto.setMessage("You have closed registrations for the " + event.getName() + " event.");
        notificationEventDto.setUserId(event.getCreationUser().getUserId());
        

		log.debug("PUT eventServiceImpl closeRegistrations eventId: {} closed", eventId);
        log.info("Event closed successfully eventId: {}", eventId);

        notificationProducer.produceNotificationEvent(notificationEventDto);
	}
	
	@Override
	public void cancelEvent(UUID userId, UUID eventId) {		
		
		Event event = findById(eventId);
		
		if(!event.getCreationUser().getUserId().equals(userId)) {
			log.warn("Access denied! userAuthenticatedId: {}", userId);
			throw new AccessDeniedException("Error: Access denied!");
		}
		
		if(!event.getEventStatus().equals(EventStatus.CANCELED) &&
		   !event.getEventStatus().equals(EventStatus.PAST)) {
			
			event.setEventStatus(EventStatus.CANCELED);
			
		} else {
			log.warn("It is not possible to cancel this event! eventId: {}", eventId);
			new BusinessException("Error: It is not possible to cancel this event!");
		}
        
        eventRepository.save(event);

        NotificationEventDTO notificationEventDto = new NotificationEventDTO();
        notificationEventDto.setTitle("An event you are registered for has been canceled.");
		if(event.getPrice().equals(0.0)) {
			notificationEventDto.setMessage("The " + event.getName() + " event has been cancelled.");
        } else {
        	notificationEventDto.setMessage("The " + event.getName() + " event has been cancelled. "
        			+ "If you have already made the payment, please contact " + event.getCreationUser().getEmail() + 
        			" to request a refund.");
        }

		for(Ticket eventTicket: event.getTickets()) {
    		User user = eventTicket.getUser();
    		notificationEventDto.setUserId(user.getUserId());
    		notificationProducer.produceNotificationEvent(notificationEventDto);
    	}

		notificationEventDto.setTitle("Event canceled");
		notificationEventDto.setMessage("The " + event.getName() + " event has been canceled.");
		for(User user: event.getStaffUsers()) {
    		notificationEventDto.setUserId(user.getUserId());
    		notificationProducer.produceNotificationEvent(notificationEventDto);
    	}

        notificationEventDto.setTitle("Event successfully canceled");
        notificationEventDto.setMessage("You canceled the " + event.getName() + " event.");
        notificationEventDto.setUserId(event.getCreationUser().getUserId());
        
        log.debug("PUT eventServiceImpl cancelEvent eventId: {} canceled", eventId);
        log.info("Event canceled successfully eventId: {}", eventId);

        notificationProducer.produceNotificationEvent(notificationEventDto);  
	}
	
	@Override
	public void insertStaff(UUID userId, UUID eventId, StaffRequestDTO staffRequestDTO) {
				
		Event event = findById(eventId);
		
		if(!event.getCreationUser().getUserId().equals(userId)) {
			log.warn("Access denied! userAuthenticatedId: {}", userId);
			throw new AccessDeniedException("Error: Access denied!");
		}
		
		User user = userService.findById(staffRequestDTO.getUserId());
		
		if(event.getEventStatus().equals(EventStatus.PAST) || 
		   event.getEventStatus().equals(EventStatus.CANCELED)) {
			log.warn("It is not possible to add new users to the staff eventId: {}", eventId);
			throw new BusinessException("Error: It is not possible to add new users to the staff!");
		}
		
		if(event.getStaffUsers().contains(user)) {
			log.warn("This user is already on staff userId: {} eventId: {}", staffRequestDTO.getUserId(), eventId);
			throw new BusinessException("Error: This user is already on staff!");
		}
		
		event.getStaffUsers().add(user);
				
		eventRepository.save(event);
		
		NotificationEventDTO notificationEventDto = new NotificationEventDTO();
        notificationEventDto.setTitle("You have been added to an event staff.");
        notificationEventDto.setMessage("You are now part of the " + event.getName() + " event staff.");
        notificationEventDto.setUserId(user.getUserId());
        
        log.debug("POST eventServiceImpl insertStaff userId: {} eventId: {} saved", staffRequestDTO.getUserId(), eventId);
        log.info("Staff saved successfully userId: {} eventId: {}", staffRequestDTO.getUserId(), eventId);

        notificationProducer.produceNotificationEvent(notificationEventDto);				
	}
	
	@Override
	public Page<EventResponseDTO> findStaffUsers(UUID userId, Pageable pageable) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if(!authentication.getName().equals(userId.toString())) {
			log.warn("Access denied! userId: {}", userId);
			throw new AccessDeniedException("Error: Access denied!");
		}
						
		Page<Event> eventPage = eventRepository.findAllByStaffUsersUserId(userId, pageable);
		
		log.debug("GET eventServiceImpl findStaffUsers userId: {} found", userId);
        log.info("Staffs found successfully userId: {}", userId);
        		
		return eventPage.map(obj -> {
			EventResponseDTO eventResponseDto = new EventResponseDTO();
			BeanUtils.copyProperties(obj, eventResponseDto);
			return eventResponseDto;}); 	
	}
	
	@Override
	public Event findById(UUID eventId) {
		
		Optional<Event> eventOptional = eventRepository.findById(eventId);
		
		Event event = eventOptional.orElseThrow(() -> {
			log.warn("Event not found! eventId: {}", eventId);
			return new ObjectNotFoundException("Error: Event not found! Id: " + eventId);
			});
		
		log.debug("GET eventServiceImpl findById eventId: {} found", eventId);
		log.info("Event found successfully eventId: {}", eventId);
		
		return event;
	}
	
	public void checkDates(Event event, EventRequestDTO eventRequestDto) {
				
		if(eventRequestDto.getStartDateTime().isBefore(eventRequestDto.getRegistrationStartDate())) {
			new BusinessException("Error: An event cannot start before ticket sales begin!");
		}
				
		if(eventRequestDto.getEndDateTime().isBefore(eventRequestDto.getStartDateTime())) {
			throw new BusinessException("Error: The end date and Time of the event cannot be earlier than the start d and time!");
		}
		
		if(eventRequestDto.getRegistrationEndDate().isBefore(eventRequestDto.getRegistrationStartDate())) {
			throw new BusinessException("Error: The registration end date cannot be earlier than the registration start date!");
		}
		
		if(eventRequestDto.getEndDateTime().isBefore(eventRequestDto.getRegistrationEndDate())) {
			throw new BusinessException("Error: The registration end date cannot be later than the event end date and time!");
		}
	}
	
	public void updateStatus(Event event) {
		
		if(event.getEventStatus() == null || (!event.getEventStatus().equals(EventStatus.CANCELED) &&
				   !event.getEventStatus().equals(EventStatus.PAST))) {
			LocalDateTime currentDate = LocalDateTime.now(ZoneId.of("UTC"));

			if(event.getEventId() != null && ticketRepository.countByEvent(event) >= event.getCapacity()) {
				event.setEventStatus(EventStatus.CLOSED_TO_REGISTRATIONS);
			} else if(event.getStartDateTime().isBefore(currentDate) && currentDate.isBefore(event.getEndDateTime())) {
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

}
