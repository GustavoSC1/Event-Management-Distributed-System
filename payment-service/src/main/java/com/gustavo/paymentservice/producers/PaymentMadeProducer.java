package com.gustavo.paymentservice.producers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.gustavo.paymentservice.dtos.rabbitmqDtos.PaymentMadeEventDTO;

@Component
public class PaymentMadeProducer {
	
	Logger log = LogManager.getLogger(PaymentMadeProducer.class);
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Value("${rabbitmq.exchange.paymentMadeExchange}")
	private String exchange;
	
	public void produceUserEvent(PaymentMadeEventDTO paymentMadeEventDto) {		
		rabbitTemplate.convertAndSend(exchange, "", paymentMadeEventDto);
		log.debug("PRODUCER paymentMadeProducer produceUserEvent paymentMadeEventDto produced {}", paymentMadeEventDto.toString());
	}

}
