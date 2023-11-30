package com.gustavo.eventservice.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.gustavo.eventservice.entities.Event;
import com.gustavo.eventservice.entities.EventTicket;
import com.gustavo.eventservice.entities.User;

public interface EventTicketRepository extends JpaRepository<EventTicket, UUID> {
	
	boolean existsByUserAndEvent(User user, Event event);
	
	long countByEvent(Event event);
	
	Page<EventTicket> findByEvent(Event event, Pageable pageable);

}
