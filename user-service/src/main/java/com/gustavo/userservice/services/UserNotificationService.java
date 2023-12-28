package com.gustavo.userservice.services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.gustavo.userservice.dtos.clientsDtos.NotificationResponseDTO;

public interface UserNotificationService {
	
	Page<NotificationResponseDTO> findAllNotificationsByUser(UUID userId, Pageable pageable);

}
