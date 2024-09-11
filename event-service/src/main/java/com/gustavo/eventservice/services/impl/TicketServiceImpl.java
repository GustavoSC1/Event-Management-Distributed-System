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

import com.gustavo.eventservice.dtos.EventResponseDTO;
import com.gustavo.eventservice.dtos.TicketRequestDTO;
import com.gustavo.eventservice.dtos.TicketResponseDTO;
import com.gustavo.eventservice.dtos.UserResponseDTO;
import com.gustavo.eventservice.dtos.rabbitmqDtos.NotificationEventDTO;
import com.gustavo.eventservice.dtos.rabbitmqDtos.PaymentEventDTO;
import com.gustavo.eventservice.dtos.rabbitmqDtos.PaymentMadeEventDTO;
import com.gustavo.eventservice.entities.Event;
import com.gustavo.eventservice.entities.Ticket;
import com.gustavo.eventservice.entities.User;
import com.gustavo.eventservice.entities.enums.EventStatus;
import com.gustavo.eventservice.producers.NotificationProducer;
import com.gustavo.eventservice.producers.PaymentProducer;
import com.gustavo.eventservice.repositories.TicketRepository;
import com.gustavo.eventservice.services.EventService;
import com.gustavo.eventservice.services.TicketService;
import com.gustavo.eventservice.services.UserService;
import com.gustavo.eventservice.services.exceptions.BusinessException;
import com.gustavo.eventservice.services.exceptions.ObjectNotFoundException;

@Service
public class TicketServiceImpl implements TicketService {
	
	Logger log = LogManager.getLogger(TicketServiceImpl.class);
	
	@Autowired
	private TicketRepository ticketRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	private NotificationProducer notificationProducer;
	
	@Autowired
	private PaymentProducer paymentProducer;

	@Override
	public void insert(UUID eventId, TicketRequestDTO ticketRequestDto) {
		
		Event event = eventService.findById(eventId);
		
		User user = userService.findById(ticketRequestDto.getUserId());
		
		if(!event.getEventStatus().equals(EventStatus.OPEN_TO_REGISTRATIONS) && 
		   !event.getEventStatus().equals(EventStatus.ONGOING)) {
			log.warn("It is not possible to register for this event! eventId: {}", eventId);
			throw new BusinessException("Error: It is not possible to register for this event!");
		}
		
		if(ticketRepository.existsByUserAndEvent(user, event)) {
			log.warn("Registration already exists! userId: {} eventId: {}", user.getUserId(), eventId);
			throw new BusinessException("Error: Registration already exists!");
		}
		
		if(ticketRepository.countByEvent(event) >= event.getCapacity()) {
			log.warn("The event has reached maximum capacity! eventId: {}", eventId);
			throw new BusinessException("Error: The event has reached maximum capacity!");
		}
				
		Ticket eventTicket = new Ticket();
		
		eventTicket.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));
		eventTicket.setEvent(event);
		eventTicket.setUser(user);
		
		String message = "";
		if(event.getPrice().equals(0.0)) {
			eventTicket.setIsPaid(true);
			
			ticketRepository.save(eventTicket);
			
			message = "You have successfully registered for the " + event.getName() + " event.";
		} else {
			eventTicket.setIsPaid(false);
			message = "You have successfully registered for the " + event.getName() + " event,"
					+ " now all you need to do is pay the ticket.";
			
			ticketRepository.save(eventTicket);
			
			PaymentEventDTO paymentEventDto = new PaymentEventDTO();			
			paymentEventDto.setTicketId(eventTicket.getTicketId());
			paymentEventDto.setUserId(user.getUserId());
			paymentEventDto.setAmount(event.getPrice());
			paymentEventDto.setEventName(event.getName());
			
			paymentProducer.producePaymentEvent(paymentEventDto);		
		}
		
		ticketRepository.save(eventTicket);
		
		NotificationEventDTO notificationEventDto = new NotificationEventDTO();
		notificationEventDto.setTitle("You registered for a new event");
        notificationEventDto.setMessage(message);
        notificationEventDto.setUserId(user.getUserId());
        
        log.debug("POST ticketServiceImpl insert ticketId: {} saved", eventTicket.getTicketId());
        log.info("Ticket saved successfully ticketId: {}", eventTicket.getTicketId());

        notificationProducer.produceNotificationEvent(notificationEventDto);		
	}	
	
	@Override
	public void setTicketPaid(PaymentMadeEventDTO paymentMadeEventDto) {
		Optional<Ticket> ticketOptional = ticketRepository.findById(paymentMadeEventDto.getTicketId());
		
		Ticket ticket = ticketOptional.orElse(null);
		
		if (ticket != null) {					
		
			ticket.setIsPaid(paymentMadeEventDto.isPaid());
			ticket.setPaymentDate(paymentMadeEventDto.getPaymentDate());
			
			log.debug("PUT ticketServiceImpl setTicketPaid ticketId: {} paid", paymentMadeEventDto.getTicketId());
	        log.info("Ticket paid successfully ticketId: {}", paymentMadeEventDto.getTicketId());
	
			ticketRepository.save(ticket);
		} else {
			log.warn("Ticket not found! ticketId: {}", paymentMadeEventDto.getTicketId());
		}
	}
	
	@Override
	public Page<TicketResponseDTO> findByEvent(UUID eventId, Pageable pageable) {
				
		Event event = eventService.findById(eventId);
		
		Page<Ticket> eventTicket = ticketRepository.findAllByEvent(event, pageable);
		
		Page<TicketResponseDTO> ticketResponseDtoPage = eventTicket.map(obj -> {
			TicketResponseDTO ticketResponseDto = new TicketResponseDTO();
			UserResponseDTO userResponseDto = new UserResponseDTO();
			BeanUtils.copyProperties(obj, ticketResponseDto);
			BeanUtils.copyProperties(obj.getUser(), userResponseDto);
			ticketResponseDto.setUserResponseDto(userResponseDto);
			return ticketResponseDto;});
		
		log.debug("GET ticketServiceImpl findByEvent eventId: {} found", eventId);
        log.info("Tickets found successfully eventId: {}", eventId);
		
		return ticketResponseDtoPage;		
	}
	
	@Override
	public Page<TicketResponseDTO> findByUser(UUID userId, Pageable pageable) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if(!authentication.getName().equals(userId.toString())) {
			log.warn("Access denied! userId: {}", userId);
			throw new AccessDeniedException("Error: Access denied!");
		}
						
		User user = userService.findById(userId);
		
		Page<Ticket> eventTicket = ticketRepository.findAllByUser(user, pageable);
		
		Page<TicketResponseDTO> ticketResponseDtoPage = eventTicket.map(obj -> {
			TicketResponseDTO ticketResponseDto = new TicketResponseDTO();
			EventResponseDTO eventResponseDto = new EventResponseDTO();
			BeanUtils.copyProperties(obj, ticketResponseDto);
			BeanUtils.copyProperties(obj.getEvent(), eventResponseDto);
			ticketResponseDto.setEventResponseDto(eventResponseDto);
			return ticketResponseDto;});
		
		log.debug("GET ticketServiceImpl findByUser userId: {} found", userId);
        log.info("Tickets found successfully userId: {}", userId);
		
		return ticketResponseDtoPage;		
	}

	@Override
	public Ticket findById(UUID ticketId) {
		Optional<Ticket> ticketOptional = ticketRepository.findById(ticketId);
		
		Ticket ticket = ticketOptional.orElseThrow(() -> {
			log.warn("Ticket not found! ticketId: {}", ticketId);
			return new ObjectNotFoundException("Error: Ticket not found! Id: " + ticketId);
			});
		
		log.debug("GET ticketServiceImpl findById ticketId: {} found", ticketId);
		log.info("Ticket found successfully ticketId: {}", ticketId);
		
		return ticket;
	}
	
}
