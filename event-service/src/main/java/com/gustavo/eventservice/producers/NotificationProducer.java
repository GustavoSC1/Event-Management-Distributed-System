package com.gustavo.eventservice.producers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.gustavo.eventservice.dtos.rabbitmqDtos.NotificationEventDTO;

@Component
public class NotificationProducer {
	
	Logger log = LogManager.getLogger(NotificationProducer.class);
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Value("${rabbitmq.exchange.notificationExchange}")
	private String notificationExchange;
	
	@Value("${rabbitmq.key.notificationKey}")
	private String notificationKey;
	
	public void produceNotificationEvent(NotificationEventDTO notificationEventDto) {		
		rabbitTemplate.convertAndSend(notificationExchange, notificationKey, notificationEventDto);
		log.debug("PRODUCER notificationProducer produceNotificationEvent notificationEventDto produced {}", notificationEventDto.toString());
	}

}
