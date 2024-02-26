package com.gustavo.userservice.services.impl;

import java.time.Instant;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.gustavo.userservice.repositories.RefreshTokenRepository;
import com.gustavo.userservice.services.ScheduleService;

@Service
public class ScheduleServiceImpl implements ScheduleService {
	
	Logger log = LogManager.getLogger(ScheduleServiceImpl.class);
	
	private static final String CRON_EXPIRED_TOKENS = "0 0 1 1/1 * ?";
	
	@Autowired
	private RefreshTokenRepository refreshTokenRepository;	
	
	@Override
	@Scheduled(cron=CRON_EXPIRED_TOKENS)
	public void deleteAllExpiredTokens() {
		refreshTokenRepository.deleteByExpiryDateBefore(Instant.now());
		log.debug("scheduleServiceImpl deleteAllExpiredTokens sent");
	}

}
