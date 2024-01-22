package com.gustavo.userservice.services.impl;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.gustavo.userservice.dtos.TokenRefreshRequestDTO;
import com.gustavo.userservice.dtos.TokenRefreshResponseDTO;
import com.gustavo.userservice.entities.RefreshToken;
import com.gustavo.userservice.repositories.RefreshTokenRepository;
import com.gustavo.userservice.repositories.UserRepository;
import com.gustavo.userservice.services.CurrentUserService;
import com.gustavo.userservice.services.RefreshTokenService;
import com.gustavo.userservice.services.exceptions.TokenRefreshException;
import com.gustavo.userservice.utils.JwtUtil;
import com.gustavo.userservice.utils.UserAuthenticated;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
	
	@Value("${jwt.refreshExpiration}")
	private Long refreshTokenDurationMs;
		
	@Autowired
	private RefreshTokenRepository refreshTokenRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CurrentUserService currentUserService;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Override
	public TokenRefreshResponseDTO refreshToken(TokenRefreshRequestDTO request) {
		String requestRefreshToken = request.getRefreshToken();
		
		return findByToken(requestRefreshToken).map(x -> verifyExpiration(x))
				.map(RefreshToken::getUser)
				.map(user -> {
					String token = jwtUtil.generateTokenFromUser(user);
					return new TokenRefreshResponseDTO(token, requestRefreshToken);
				}).orElseThrow(() -> new TokenRefreshException("Error: Refresh token is not in database!"));				
	}
	
	@Override
	public void logoutUser() {
		UserAuthenticated userAuthenticated = currentUserService.getCurrentUser();
		UUID userId = userAuthenticated.getId();
		deleteByUserId(userId);
	}
	
	@Override
	public RefreshToken createRefreshToken(UUID userId) {
		RefreshToken refreshToken = new RefreshToken();
		
		refreshToken.setUser(userRepository.findById(userId).get());
		refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
		refreshToken.setToken(UUID.randomUUID().toString());
		
		refreshToken = refreshTokenRepository.save(refreshToken);
		return refreshToken;
	}
	
	@Override
	public Optional<RefreshToken> findByToken(String token) {
		return refreshTokenRepository.findByToken(token);
	}
	
	@Override
	public RefreshToken verifyExpiration(RefreshToken token) {
		if(token.getExpiryDate().compareTo(Instant.now()) < 0) {
			refreshTokenRepository.delete(token);
			throw new TokenRefreshException("Error: Refresh token was expired. Please make a new signin request");
		}
		
		return token;
	}
	
	@Override
	public void deleteByUserId(UUID userId) {
		refreshTokenRepository.deleteByUser(userRepository.findById(userId).get());
	}

}
