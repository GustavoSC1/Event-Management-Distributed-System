package com.gustavo.userservice.clients;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.gustavo.userservice.configs.FeignConfig;
import com.gustavo.userservice.dtos.clientsDtos.NotificationResponseDTO;

@FeignClient(name = "notification-service", configuration = FeignConfig.class)
public interface NotificationClient {
	
	@GetMapping(value = "/notifications/users/{userId}")
	Page<NotificationResponseDTO> findAllNotificationsByUser(
			@PathVariable(value = "userId") UUID userId,
			Pageable page);

}
