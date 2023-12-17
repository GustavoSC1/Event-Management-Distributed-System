package com.gustavo.userservice.producers;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.gustavo.userservice.dtos.rabbitmqDtos.UserEventDto;

@Component
public class UserProducer {
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Value("${rabbitmq.exchange.name}")
	private String exchange;
	
	public void produceUserEvent(UserEventDto userEventDto) {		
		rabbitTemplate.convertAndSend(exchange, "", userEventDto);
	}

}
