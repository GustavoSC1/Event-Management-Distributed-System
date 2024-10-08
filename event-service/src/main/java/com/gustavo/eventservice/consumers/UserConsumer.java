package com.gustavo.eventservice.consumers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.gustavo.eventservice.dtos.rabbitmqDtos.UserEventDTO;
import com.gustavo.eventservice.entities.User;
import com.gustavo.eventservice.entities.enums.ActionType;
import com.gustavo.eventservice.services.UserService;

@Component
public class UserConsumer {
	
	Logger log = LogManager.getLogger(UserConsumer.class);
		
	@Autowired
	private UserService userService;
	
	@RabbitListener(queues = "${rabbitmq.queue.userQueue}")
	public void onUserCreated(@Payload UserEventDTO userEventDto) {
		
		User user = new User();
				
		BeanUtils.copyProperties(userEventDto, user);
		
		switch (ActionType.valueOf(userEventDto.getActionType())) {
			case CREATE:
			case UPDATE:
				userService.insert(user);				
				break;
			case DELETE:
				userService.delete(user.getUserId());
				break;
		}
		
		log.debug("CONSUMER userConsumer onUserCreated userEventDto consumed {}", userEventDto.toString());
	}

}
