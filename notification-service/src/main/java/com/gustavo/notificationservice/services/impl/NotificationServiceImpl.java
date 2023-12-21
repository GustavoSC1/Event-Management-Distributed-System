package com.gustavo.notificationservice.services.impl;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.gustavo.notificationservice.dtos.NotificationResponseDTO;
import com.gustavo.notificationservice.entities.Notification;
import com.gustavo.notificationservice.entities.enums.NotificationStatus;
import com.gustavo.notificationservice.repositories.NotificationRepository;
import com.gustavo.notificationservice.services.NotificationService;
import com.gustavo.notificationservice.services.exceptions.ObjectNotFoundException;

@Service
public class NotificationServiceImpl implements NotificationService {
	
	@Autowired
	private NotificationRepository notificationRepository;
	
	@Override
	public NotificationResponseDTO insert(Notification notification) {
		notificationRepository.save(notification);
		
		NotificationResponseDTO notificationResponseDto = new NotificationResponseDTO();
		BeanUtils.copyProperties(notification, notificationResponseDto);
		
		return notificationResponseDto;
	}
	
	@Override
	public Page<NotificationResponseDTO> findByUser(UUID userId, Pageable pageable) {
		
		Page<Notification> notificationPage = notificationRepository.findAllByUserIdAndNotificationStatus(userId, NotificationStatus.CREATED, pageable);
	
		Page<NotificationResponseDTO> notificationResponseDtoPage = notificationPage.map(obj -> {
			NotificationResponseDTO notificationResponseDto = new NotificationResponseDTO();
			BeanUtils.copyProperties(obj, notificationResponseDto);
			return notificationResponseDto;});
		
		return notificationResponseDtoPage;
	}
	
	@Override
	public void markAsRead(UUID userId, UUID notificationId) {
		
		Optional<Notification> notificationOptional = notificationRepository.findByNotificationIdAndUserId(notificationId, userId);
		
		Notification notification = notificationOptional.orElseThrow(() -> 
			new ObjectNotFoundException("Notification not found! Id: " + notificationId + " User Id: " + userId));
		
		notification.setNotificationStatus(NotificationStatus.READ);
		
		notificationRepository.save(notification);
	}
	

}
