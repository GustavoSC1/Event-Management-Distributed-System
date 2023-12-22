package com.gustavo.userservice.producers;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.gustavo.userservice.dtos.rabbitmqDtos.UserEventDTO;

@Component
public class UserProducer {
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Value("${rabbitmq.exchange.userExchange}")
	private String userExchange;
	
	@Value("${rabbitmq.key.userKey}")
	private String userKey;
	
	public void produceUserEvent(UserEventDTO userEventDto) {		
		rabbitTemplate.convertAndSend(userExchange, userKey, userEventDto);
	}

}
