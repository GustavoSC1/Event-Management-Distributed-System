package com.gustavo.eventservice.producers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.gustavo.eventservice.dtos.rabbitmqDtos.PaymentEventDTO;

@Component
public class PaymentProducer {
	
	Logger log = LogManager.getLogger(PaymentProducer.class);
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Value("${rabbitmq.exchange.paymentExchange}")
	private String paymentExchange;
	
	@Value("${rabbitmq.key.paymentKey}")
	private String paymentKey;
	
	public void producePaymentEvent(PaymentEventDTO paymentEventDto) {		
		rabbitTemplate.convertAndSend(paymentExchange, paymentKey, paymentEventDto);
		log.debug("PRODUCER paymentProducer producePaymentEvent paymentEventDto produced {}", paymentEventDto.toString());
	}

}
