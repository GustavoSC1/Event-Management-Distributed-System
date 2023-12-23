package com.gustavo.paymentservice.configs;

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
	
	@Value("${rabbitmq.queue.paymentQueue}")
	private String paymentQueue;
	
	@Value("${rabbitmq.exchange.paymentExchange}")
	private String paymentExchange;
	
	@Value("${rabbitmq.exchange.paymentMadeExchange}")
	private String paymentMadeExchange;
	
	@Value("${rabbitmq.key.paymentKey}")
	private String paymentKey;
	
	@Bean
	public Queue paymentQueue() {		
		return new Queue(paymentQueue, true);
	}
	
	@Bean
	public Binding paymentBinding(Queue paymentQueue) {
		DirectExchange exchange = new DirectExchange(paymentExchange);
		exchange.setIgnoreDeclarationExceptions(true);
		return BindingBuilder.bind(paymentQueue).to(exchange).with(paymentKey);
	}
	
	@Bean
	public FanoutExchange paymentMadeExchange() {
		return new FanoutExchange(paymentMadeExchange);
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
