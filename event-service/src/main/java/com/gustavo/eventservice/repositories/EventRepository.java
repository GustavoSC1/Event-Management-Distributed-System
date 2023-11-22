package com.gustavo.eventservice.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gustavo.eventservice.entities.Event;

public interface EventRepository extends JpaRepository<Event, UUID> {

}
