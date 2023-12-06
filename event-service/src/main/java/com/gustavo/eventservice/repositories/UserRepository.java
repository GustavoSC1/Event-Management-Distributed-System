package com.gustavo.eventservice.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.gustavo.eventservice.entities.User;

public interface UserRepository extends JpaRepository<User, UUID> {
	
	@Transactional(readOnly = true)
	Page<User> findAllByStaffEventsEventId(UUID eventId, Pageable pageable);

}
