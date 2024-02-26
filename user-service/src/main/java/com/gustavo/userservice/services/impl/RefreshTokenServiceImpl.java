package com.gustavo.userservice.services.impl;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
	
	Logger log = LogManager.getLogger(RefreshTokenServiceImpl.class);
	
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
		
		TokenRefreshResponseDTO tokenRefreshResponseDto = findByToken(requestRefreshToken).map(x -> verifyExpiration(x))
				.map(RefreshToken::getUser)
				.map(user -> {
					String token = jwtUtil.generateTokenFromUser(user);
					log.debug("POST refreshTokenServiceImpl refreshToken userId: {} refresh", user.getUserId());
			        log.info("Token updated successfully userId {}", user.getUserId());
					return new TokenRefreshResponseDTO(token, requestRefreshToken);
				}).orElseThrow(() -> {
					log.warn("Refresh token is not in database! refreshToken: {}", request.getRefreshToken());
					return new TokenRefreshException("Error: Refresh token is not in database!");					
				});		
				
		return tokenRefreshResponseDto;
	}
	
	@Override
	public void logoutUser() {
		UserAuthenticated userAuthenticated = currentUserService.getCurrentUser();
		log.debug("POST refreshTokenServiceImpl logoutUser userId: {} received", userAuthenticated.getId());		
		UUID userId = userAuthenticated.getId();
		deleteByUserId(userId);
		
		log.debug("DELETE refreshTokenServiceImpl logoutUser userId: {} logout", userId);
        log.info("Logout completed successfully userId {}", userId);
	}
	
	@Override
	public RefreshToken createRefreshToken(UUID userId) {
		RefreshToken refreshToken = new RefreshToken();
		
		refreshToken.setUser(userRepository.findById(userId).get());
		refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
		refreshToken.setToken(UUID.randomUUID().toString());
		
		refreshToken = refreshTokenRepository.save(refreshToken);
		
		log.debug("POST refreshTokenServiceImpl createRefreshToken userId: {} received", userId);
        log.info("Refresh token created successfully userId {}", userId);
        
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
			log.warn("Refresh token was expired! refreshToken: {}", token);
			throw new TokenRefreshException("Error: Refresh token was expired. Please make a new signin request");
		}
		
		return token;
	}
	
	@Override
	public void deleteByUserId(UUID userId) {
		refreshTokenRepository.deleteByUser(userRepository.findById(userId).get());
		log.debug("DELETE refreshTokenServiceImpl deleteByUserId userId: {} deleted", userId);
        log.info("Refresh Token successfully deleted userId {}", userId);
	}

}
