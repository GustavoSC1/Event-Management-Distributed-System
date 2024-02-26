package com.gustavo.notificationservice.consumers;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.gustavo.notificationservice.dtos.rabbitmqDtos.PaymentMadeEventDTO;
import com.gustavo.notificationservice.entities.Notification;
import com.gustavo.notificationservice.entities.enums.NotificationStatus;
import com.gustavo.notificationservice.services.NotificationService;

@Component
public class PaymentMadeConsumer {
	
	Logger log = LogManager.getLogger(PaymentMadeConsumer.class);
	
	@Autowired
	private NotificationService notificationService;
	
	@RabbitListener(queues = "${rabbitmq.queue.paymentMadeQueue}")
	public void onPaymentMade(@Payload PaymentMadeEventDTO paymentMadeEventDto) {
		
		Notification notification = new Notification();
		notification.setTitle("Payment confirmation");
		notification.setMessage("Payment " + paymentMadeEventDto.getPaymentCode() +" for the "
				+ paymentMadeEventDto.getEventName() + " event ticket has been confirmed.");
		notification.setUserId(paymentMadeEventDto.getUserId());
		notification.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
		notification.setNotificationStatus(NotificationStatus.CREATED);

		notificationService.insert(notification);		
		log.debug("CONSUMER paymentMadeConsumer onPaymentMade paymentMadeEventDto consumed {}", paymentMadeEventDto.toString());
	}

}
