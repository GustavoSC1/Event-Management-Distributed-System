package com.gustavo.paymentservice.consumers;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
	
	Logger log = LogManager.getLogger(PaymentConsumer.class);
	
	@Autowired
	private PaymentService paymentService;
	
	@RabbitListener(queues = "${rabbitmq.queue.paymentQueue}")
	public void onPaymentCreated(@Payload PaymentEventDTO paymentEventDto) {
		
		Payment payment = new Payment();
		BeanUtils.copyProperties(paymentEventDto, payment);
		payment.setPaymentCode(UUID.randomUUID().toString());
		payment.setPaymentRequestDate(LocalDateTime.now(ZoneId.of("UTC")));
		payment.setDueDate(LocalDateTime.now(ZoneId.of("UTC")).plusDays(2));
		payment.setPaid(false);
		payment.setPaymentDate(null);
		
		paymentService.insert(payment);	
		log.debug("CONSUMER paymentConsumer onPaymentCreated paymentEventDto consumed {}", paymentEventDto.toString());
	}

}
