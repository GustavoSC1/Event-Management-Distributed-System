package com.gustavo.eventservice.consumers;

import com.gustavo.eventservice.dtos.rabbitmqDtos.PaymentMadeEventDTO;
import com.gustavo.eventservice.services.TicketService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class PaymentMadeConsumer {
	
	Logger log = LogManager.getLogger(PaymentMadeConsumer.class);
	
	@Autowired
	private TicketService ticketService;
	
	@RabbitListener(queues = "${rabbitmq.queue.paymentMadeQueue}")
	public void onPaymentMade(@Payload PaymentMadeEventDTO paymentMadeEventDto) {
		ticketService.setTicketPaid(paymentMadeEventDto);
		log.debug("CONSUMER paymentMadeConsumer onPaymentMade paymentMadeEventDto consumed {}", paymentMadeEventDto.toString());
	}

}
