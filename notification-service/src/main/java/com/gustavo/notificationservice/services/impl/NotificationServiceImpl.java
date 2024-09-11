package com.gustavo.notificationservice.services.impl;

import java.util.Optional;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.gustavo.notificationservice.dtos.NotificationResponseDTO;
import com.gustavo.notificationservice.entities.Notification;
import com.gustavo.notificationservice.entities.enums.NotificationStatus;
import com.gustavo.notificationservice.repositories.NotificationRepository;
import com.gustavo.notificationservice.services.NotificationService;
import com.gustavo.notificationservice.services.exceptions.ObjectNotFoundException;

@Service
public class NotificationServiceImpl implements NotificationService {
	
	Logger log = LogManager.getLogger(NotificationServiceImpl.class);
	
	@Autowired
	private NotificationRepository notificationRepository;
	
	@Override
	public NotificationResponseDTO insert(Notification notification) {
				
		notificationRepository.save(notification);
		
		NotificationResponseDTO notificationResponseDto = new NotificationResponseDTO();
		BeanUtils.copyProperties(notification, notificationResponseDto);
		
		log.debug("POST notificationServiceImpl insert notificationId saved {}", notificationResponseDto.getNotificationId());
        log.info("Notification saved successfully notificationId {}", notificationResponseDto.getNotificationId());
		
		return notificationResponseDto;
	}
		
	@Override
	public Page<NotificationResponseDTO> findByUser(UUID userId, Pageable pageable) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
				
		if(!authentication.getName().equals(userId.toString())) {
			log.warn("Access denied! userId: {}", userId);
			throw new AccessDeniedException("Error: Access denied!");
		}
				
		Page<Notification> notificationPage = notificationRepository.findAllByUserIdAndNotificationStatus(userId, NotificationStatus.CREATED, pageable);
		
		Page<NotificationResponseDTO> notificationResponseDtoPage = notificationPage.map(obj -> {
			NotificationResponseDTO notificationResponseDto = new NotificationResponseDTO();
			BeanUtils.copyProperties(obj, notificationResponseDto);
			return notificationResponseDto;});
		
		log.debug("GET userServiceImpl findByUser userId: {} found", userId);
        log.info("Notifications found successfully userId: {}", userId);
        
		return notificationResponseDtoPage;
	}
	
	@Override
	public void markAsRead(UUID userId, UUID notificationId) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if(!authentication.getName().equals(userId.toString())) {
			log.warn("Access denied! userId: {}", userId);
			throw new AccessDeniedException("Error: Access denied!");
		}
		
		Optional<Notification> notificationOptional = notificationRepository.findByNotificationIdAndUserId(notificationId, userId);
		
		Notification notification = notificationOptional.orElseThrow(() -> {
				log.warn("Notification not found! notificationId: {} userId: {}", notificationId, userId);
				return new ObjectNotFoundException("Notification not found! Id: " + notificationId + " User Id: " + userId);
				});
		
		notification.setNotificationStatus(NotificationStatus.READ);
		
		log.debug("PATCH userServiceImpl updatePassword userId: {} updated", userId);
        log.info("User password updated successfully userId {}", userId);
		
		notificationRepository.save(notification);
	}
	
}
