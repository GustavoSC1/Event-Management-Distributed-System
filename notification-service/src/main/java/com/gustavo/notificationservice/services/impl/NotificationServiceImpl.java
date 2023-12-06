package com.gustavo.notificationservice.services.impl;

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

@Service
public class NotificationServiceImpl implements NotificationService {
	
	@Autowired
	private NotificationRepository notificationRepository;
	
	public Page<NotificationResponseDTO> findByUser(UUID userId, Pageable pageable) {
		
		Page<Notification> notificationPage = notificationRepository.findAllByUserIdAndNotificationStatus(userId, NotificationStatus.CREATED, pageable);
	
		Page<NotificationResponseDTO> notificationResponseDtoPage = notificationPage.map(obj -> {
			NotificationResponseDTO notificationResponseDto = new NotificationResponseDTO();
			BeanUtils.copyProperties(obj, notificationResponseDto);
			return notificationResponseDto;});
		
		return notificationResponseDtoPage;
	}
	

}
