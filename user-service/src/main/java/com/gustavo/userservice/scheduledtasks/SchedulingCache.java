package com.gustavo.userservice.scheduledtasks;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SchedulingCache {
	
	Logger log = LogManager.getLogger(SchedulingCache.class);
	
	@Scheduled(fixedDelay = 3, timeUnit = TimeUnit.MINUTES)
	@CacheEvict(value = "users", allEntries = true)
	public void evictAllUsersCache() {
		log.debug("'Users' cache cleared automatically: {}",  LocalDateTime.now());
        log.info("'Users' cache cleared automatically.: {}",  LocalDateTime.now());
	}
}
