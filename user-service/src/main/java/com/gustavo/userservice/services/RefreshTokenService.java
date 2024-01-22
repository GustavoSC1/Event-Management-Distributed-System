package com.gustavo.userservice.services;

import java.util.Optional;
import java.util.UUID;

import com.gustavo.userservice.dtos.TokenRefreshRequestDTO;
import com.gustavo.userservice.dtos.TokenRefreshResponseDTO;
import com.gustavo.userservice.entities.RefreshToken;

public interface RefreshTokenService {
	
	TokenRefreshResponseDTO refreshToken(TokenRefreshRequestDTO request);
	
	void logoutUser();
	
	RefreshToken createRefreshToken(UUID userId);
	
	Optional<RefreshToken> findByToken(String token);
	
	RefreshToken verifyExpiration(RefreshToken token);
	
	void deleteByUserId(UUID userId);

}
