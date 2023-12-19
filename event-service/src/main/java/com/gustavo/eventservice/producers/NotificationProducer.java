package com.gustavo.eventservice.producers;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.gustavo.eventservice.dtos.rabbitmqDtos.NotificationEventDto;

@Component
public class NotificationProducer {
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Value("${rabbitmq.exchange.notificationExchange}")
	private String notificationExchange;
	
	@Value("${rabbitmq.key.notificationKey}")
	private String notificationKey;
	
	public void produceNotificationEvent(NotificationEventDto notificationEventDto) {		
		rabbitTemplate.convertAndSend(notificationExchange, notificationKey, notificationEventDto);
	}

}
