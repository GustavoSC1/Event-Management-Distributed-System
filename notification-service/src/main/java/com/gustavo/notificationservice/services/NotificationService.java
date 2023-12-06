package com.gustavo.notificationservice.services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.gustavo.notificationservice.dtos.NotificationResponseDTO;

public interface NotificationService {
	
	Page<NotificationResponseDTO> findByUser(UUID userId, Pageable pageable);

}
