package com.gustavo.paymentservice.consumers;

import java.util.UUID;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.gustavo.paymentservice.dtos.rabbitmqDtos.PaymentEventDTO;
import com.gustavo.paymentservice.entities.Payment;
import com.gustavo.paymentservice.services.PaymentService;

@Component
public class PaymentConsumer {
	
	@Autowired
	private PaymentService paymentService;
	
	@RabbitListener(queues = "${rabbitmq.queue.paymentQueue}")
	public void onPaymentCreated(@Payload PaymentEventDTO paymentEventDto) {
		
		Payment payment = new Payment();
		BeanUtils.copyProperties(paymentEventDto, payment);
		payment.setPaymentCode(UUID.randomUUID().toString());
		payment.setPaid(false);
		payment.setPaymentDate(null);
		
		paymentService.insert(payment);	
	}

}
