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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class RabbitmqConfig {
	
	@Value("${rabbitmq.queue.userQueue}")
	private String userQueue;
	
	@Value("${rabbitmq.exchange.userExchange}")
	private String userExchange;
	
	@Value("${rabbitmq.key.userKey}")
	private String userKey;
	
	@Value("${rabbitmq.exchange.notificationExchange}")
	private String notificationExchange;
	
	@Value("${rabbitmq.exchange.paymentExchange}")
	private String paymentExchange;
	
	@Value("${rabbitmq.queue.paymentMadeQueue}")
	private String paymentMadeQueue;
	
	@Value("${rabbitmq.exchange.paymentMadeExchange}")
	private String paymentMadeExchange;
		
	@Bean
	public Queue userQueue() {		
		return new Queue(userQueue, true);
	}
	
	@Bean
	public Binding userBinding(Queue userQueue) {
		DirectExchange exchange = new DirectExchange(userExchange);
		// Ignorar exceções, como propriedades incompatíveis ao declarar.
		exchange.setIgnoreDeclarationExceptions(true);
		return BindingBuilder.bind(userQueue).to(exchange).with(userKey);
	}
	
	@Bean
	public Queue paymentMadeQueue() {		
		return new Queue(paymentMadeQueue, true);
	}
	
	@Bean
	public Binding paymentMadeBinding(Queue paymentMadeQueue) {
		FanoutExchange exchange = new FanoutExchange(paymentMadeExchange);
		exchange.setIgnoreDeclarationExceptions(true);
		return BindingBuilder.bind(paymentMadeQueue).to(exchange);
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
		ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return new Jackson2JsonMessageConverter(objectMapper);
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
