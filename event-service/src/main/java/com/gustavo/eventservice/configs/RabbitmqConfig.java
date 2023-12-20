package com.gustavo.eventservice.configs;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {
	
	@Value("${rabbitmq.queue.userQueue}")
	private String userQueue;
	
	@Value("${rabbitmq.exchange.userExchange}")
	private String userExchange;
	
	@Value("${rabbitmq.exchange.notificationExchange}")
	private String notificationExchange;
	
	@Value("${rabbitmq.exchange.paymentExchange}")
	private String paymentExchange;
	
	@Bean
	public Queue userQueue() {		
		return new Queue(userQueue, true);
	}
	
	@Bean
	public Binding userBinding(Queue userQueue) {
		FanoutExchange exchange = new FanoutExchange(userExchange);
		// Ignorar exceções, como propriedades incompatíveis ao declarar.
		exchange.setIgnoreDeclarationExceptions(true);
		return BindingBuilder.bind(userQueue).to(exchange);
	}
	
	@Bean
	public DirectExchange notificationExchange() {
		return new DirectExchange(notificationExchange);
	}
	
	@Bean
	public DirectExchange paymentExchange() {
		return new DirectExchange(paymentExchange);
	}
	
	@Bean
	public Jackson2JsonMessageConverter messageConverter() {
		return new Jackson2JsonMessageConverter();
	}
	
	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, 
											Jackson2JsonMessageConverter messageConverter) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(messageConverter);
		return rabbitTemplate;
	}
	
	@Bean
	public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
		return new RabbitAdmin(connectionFactory);
	}
	
	@Bean
	public ApplicationListener<ApplicationReadyEvent> applicationReadyEventApplicationListener(RabbitAdmin rabbitAdmin) {
		return event -> rabbitAdmin.initialize();
	}

}
