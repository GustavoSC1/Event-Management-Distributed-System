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
			paymentEventDto.setDetails(event.getName());
			
			paymentProducer.producePaymentEvent(paymentEventDto);		
		}
		
		ticketRepository.save(eventTicket);
		
		NotificationEventDTO notificationEventDto = new NotificationEventDTO();
		notificationEventDto.setTitle("You registered for a new event");
        notificationEventDto.setMessage(message);
        notificationEventDto.setUserId(user.getUserId());

        notificationProducer.produceNotificationEvent(notificationEventDto);		
	}	
	
	@Override
	public void setTicketPaid(PaymentMadeEventDTO paymentMadeEventDto) {
		Ticket ticket = findById(paymentMadeEventDto.getTicketId());
		
		ticket.setIsPaid(paymentMadeEventDto.isPaid());
		ticket.setPaymentDate(paymentMadeEventDto.getPaymentDate());

		ticketRepository.save(ticket);
	}

	@Override
	public Ticket findById(UUID ticketId) {
		Optional<Ticket> ticketOptional = ticketRepository.findById(ticketId);
		
		return ticketOptional.orElseThrow(() -> new ObjectNotFoundException("Ticket not found! Id: " + ticketId));
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
		
		return ticketResponseDtoPage;		
	}
	
	@Override
	public Page<TicketResponseDTO> findByUser(UUID userId, Pageable pageable) {
		
		User user = userService.findById(userId);
		
		Page<Ticket> eventTicket = ticketRepository.findAllByUser(user, pageable);
		
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
