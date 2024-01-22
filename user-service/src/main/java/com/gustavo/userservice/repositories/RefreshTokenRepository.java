package com.gustavo.userservice.repositories;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;

import com.gustavo.userservice.entities.RefreshToken;
import com.gustavo.userservice.entities.User;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
	
	Optional<RefreshToken> findByToken(String token);
		
    @Transactional
	@Modifying
	void deleteByUser(User user);
    
	@Transactional
	@Modifying
	void deleteByExpiryDateBefore(Instant instant);

}
