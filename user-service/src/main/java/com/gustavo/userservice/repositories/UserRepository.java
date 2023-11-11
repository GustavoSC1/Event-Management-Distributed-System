package com.gustavo.userservice.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gustavo.userservice.entities.User;

public interface UserRepository extends JpaRepository<User, UUID> {
	
	boolean existsByUsername(String username);
	
	boolean existsByEmail(String email);


}
