package com.gustavo.eventservice.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.gustavo.eventservice.dtos.UserResponseDTO;
import com.gustavo.eventservice.dtos.rabbitmqDtos.NotificationEventDTO;
import com.gustavo.eventservice.entities.Event;
import com.gustavo.eventservice.entities.Ticket;
import com.gustavo.eventservice.entities.User;
import com.gustavo.eventservice.entities.enums.EventStatus;
import com.gustavo.eventservice.producers.NotificationProducer;
import com.gustavo.eventservice.repositories.EventRepository;
import com.gustavo.eventservice.repositories.UserRepository;
import com.gustavo.eventservice.services.UserService;
import com.gustavo.eventservice.services.exceptions.ObjectNotFoundException;

@Service
public class UserServiceImpl implements UserService {
	
	Logger log = LogManager.getLogger(UserServiceImpl.class);
	
	@Autowired
	private UserRepository userRepository;
		
	@Autowired
	private EventRepository eventRepository;
	
	@Autowired
	private NotificationProducer notificationProducer;
	
	@Override
	@CacheEvict(value = "users", key = "#user.userId")
	public UserResponseDTO insert(User user) {
		userRepository.save(user);
		
		UserResponseDTO userResponseDto = new UserResponseDTO();
		BeanUtils.copyProperties(user, userResponseDto);
		
		log.debug("POST userServiceImpl insert userId: {} saved", userResponseDto.getUserId());
        log.info("User saved successfully userId: {}", userResponseDto.getUserId());
		
		return userResponseDto;
	}
	
	@Override
	@Cacheable(value = "users", key = "#userId")
	public User findById(UUID userId) {
		Optional<User> userOptional = userRepository.findById(userId);
		
		User user = userOptional.orElseThrow(() -> {
			log.warn("User not found! userId: {}", userId);
			return new ObjectNotFoundException("Error: User not found! Id: " + userId);});
		
		log.debug("GET userServiceImpl findById userId: {} found", userId);
		log.info("User found successfully userId: {}", userId);
		
		return user;
	}
	
	@Override
	public Page<UserResponseDTO> findStaffEvent(UUID eventId, Pageable pageable) {
		
		Page<User> userPage = userRepository.findAllByStaffEventsEventId(eventId, pageable);				
		
		log.debug("GET userServiceImpl findStaffEvent eventId: {} found", eventId);
        log.info("Staffs found successfully eventId: {}", eventId);
        
		return userPage.map(obj -> {
			UserResponseDTO userResponseDto = new UserResponseDTO();
			BeanUtils.copyProperties(obj, userResponseDto);
			return userResponseDto;});		
	}

	@Override
	@CacheEvict(value = "users", key = "#userId")
	public void delete(UUID userId) {
		
		Optional<User> userOptional = userRepository.findById(userId);
		
		User user = userOptional.orElse(null);
		
		if (user != null) {
			NotificationEventDTO notificationEventDto = new NotificationEventDTO();
			notificationEventDto.setTitle("An event you are registered for has been canceled.");
	
			List<Event> createdEvents = eventRepository.findAllCreatedEventsByUser(userId);
			
			for(Event event: createdEvents) {
				notificationEventDto.setMessage("The " + event.getName() + " event has been cancelled.");
			
				if(!event.getEventStatus().equals(EventStatus.CANCELED) &&
						   !event.getEventStatus().equals(EventStatus.PAST)) {
			
					if(event.getPrice().equals(0.0)) {
						notificationEventDto.setMessage("The " + event.getName() + " event has been cancelled.");
			        } else {		   
			        	notificationEventDto.setMessage("The " + event.getName() + " event has been cancelled. "
			        			+ "If you have already made the payment, please contact " + event.getCreationUser().getEmail() + 
			        			" to request a refund.");
			        }
					
					for(Ticket eventTicket: event.getTickets()) {
			    		notificationEventDto.setUserId(eventTicket.getUser().getUserId());
			    		notificationProducer.produceNotificationEvent(notificationEventDto);
			    	}
					
					notificationEventDto.setTitle("Event canceled");
					notificationEventDto.setMessage("The " + event.getName() + " event has been canceled.");
					for(User staffUser: event.getStaffUsers()) {
			    		notificationEventDto.setUserId(staffUser.getUserId());
			    		notificationProducer.produceNotificationEvent(notificationEventDto);
			    	}						
							
				} 
	
				eventRepository.deleteById(event.getEventId());	
			}		
		
			log.debug("DELETE userServiceImpl delete userId: {} deleted", userId);
	        log.info("User successfully deleted userId: {}", userId);
			userRepository.delete(user);
		} else {
			log.warn("User not found! userId: {}", userId);
		}
		
	}
		
}
