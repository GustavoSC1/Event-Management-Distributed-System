package com.gustavo.eventservice.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.gustavo.eventservice.entities.Event;
import com.gustavo.eventservice.entities.Ticket;
import com.gustavo.eventservice.entities.User;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {
	
	boolean existsByUserAndEvent(User user, Event event);
	
	long countByEvent(Event event);
	
	Page<Ticket> findAllByEvent(Event event, Pageable pageable);
	
	Page<Ticket> findAllByUser(User user, Pageable pageable);

}
