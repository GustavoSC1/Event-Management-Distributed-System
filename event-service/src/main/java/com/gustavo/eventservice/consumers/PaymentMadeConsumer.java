package com.gustavo.eventservice.consumers;

import com.gustavo.eventservice.dtos.rabbitmqDtos.PaymentMadeEventDTO;
import com.gustavo.eventservice.services.TicketService;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class PaymentMadeConsumer {
	
	@Autowired
	private TicketService ticketService;
	
	@RabbitListener(queues = "${rabbitmq.queue.paymentMadeQueue}")
	public void onPaymentMade(@Payload PaymentMadeEventDTO paymentMadeEventDto) {
		ticketService.setTicketPaid(paymentMadeEventDto);
	}

}
