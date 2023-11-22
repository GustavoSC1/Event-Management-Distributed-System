package com.gustavo.eventservice.services;

import java.util.UUID;

import com.gustavo.eventservice.entities.User;

public interface UserService {
	
	User findById(UUID userId);

}
