package com.gustavo.notificationservice.services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.gustavo.notificationservice.dtos.NotificationResponseDTO;
import com.gustavo.notificationservice.entities.Notification;

public interface NotificationService {
	
	NotificationResponseDTO insert(Notification notification);
	
	Page<NotificationResponseDTO> findByUser(UUID userId, Pageable pageable);
	
	void markAsRead(UUID userId, UUID notificationId);

}
