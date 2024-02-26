package com.gustavo.notificationservice.consumers;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.gustavo.notificationservice.dtos.rabbitmqDtos.NotificationEventDTO;
import com.gustavo.notificationservice.entities.Notification;
import com.gustavo.notificationservice.entities.enums.NotificationStatus;
import com.gustavo.notificationservice.services.NotificationService;

@Component
public class NotificationConsumer {
	
	Logger log = LogManager.getLogger(NotificationConsumer.class);
		
	@Autowired
	private NotificationService notificationService;
	
	@RabbitListener(queues = "${rabbitmq.queue.notificationQueue}")
	public void onNotificationCreated(@Payload NotificationEventDTO notificationEventDto) {
		
		Notification notification = new Notification();
		BeanUtils.copyProperties(notificationEventDto, notification);
		notification.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
		notification.setNotificationStatus(NotificationStatus.CREATED);
		
		notificationService.insert(notification);
		log.debug("CONSUMER notificationConsumer onNotificationCreated notificationEventDto consumed {}", notificationEventDto.toString());
	}

}
