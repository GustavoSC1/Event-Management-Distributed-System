package com.gustavo.notificationservice.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.gustavo.notificationservice.entities.Notification;
import com.gustavo.notificationservice.entities.enums.NotificationStatus;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
	
	Page<Notification> findAllByUserIdAndNotificationStatus(UUID userId, NotificationStatus notificationStatus, Pageable pageable);

}
