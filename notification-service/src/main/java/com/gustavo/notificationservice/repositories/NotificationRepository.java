package com.gustavo.notificationservice.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gustavo.notificationservice.entities.Notification;
import com.gustavo.notificationservice.entities.enums.NotificationStatus;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {
	
	@Transactional(readOnly = true)
	Optional<Notification> findByNotificationIdAndUserId(UUID notificationId, UUID userId);
	
	@Transactional(readOnly = true)
	Page<Notification> findAllByUserIdAndNotificationStatus(UUID userId, NotificationStatus notificationStatus, Pageable pageable);

}
