package com.gustavo.keycloakUserSync;

import java.io.IOException;

import org.keycloak.Config.Scope;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventListenerProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class CustomEventListenerProviderFactory implements EventListenerProviderFactory {
	
	Logger log = LoggerFactory.getLogger(CustomEventListenerProviderFactory.class);
	
	private static final String PROVIDER_ID = "custom-user-registration";
	private ConnectionFactory connectionFactory;
	private Connection connection;
	private Channel channel;

	@Override
	public EventListenerProvider create(KeycloakSession session) {
		checkConnectionAndChannel();
		return new CustomEventListenerProvider(channel, session);
	}
	
	private synchronized void checkConnectionAndChannel() {
        try {
        	
            if (connection == null || !connection.isOpen()) {
                this.connection = connectionFactory.newConnection();
            }
            if (channel == null || !channel.isOpen()) {
                channel = connection.createChannel();
            }
        }
        catch (IOException | TimeoutException e) {
            log.error("keycloak-to-rabbitmq ERROR on connection to rabbitmq", e);
        }
    }
	
	@Override
	public void init(Scope config) {
		this.connectionFactory = new ConnectionFactory();		
		this.connectionFactory.setPort(5672);
		this.connectionFactory.setHost("rabbitmq");
		this.connectionFactory.setVirtualHost("/");
		this.connectionFactory.setUsername("user");
		this.connectionFactory.setPassword("password");
		this.connectionFactory.setAutomaticRecoveryEnabled(true);
	}

	@Override
	public void postInit(KeycloakSessionFactory factory) {
		// TODO Auto-generated method stub		
	}
	
	@Override
	public void close() {		
		try {
            channel.close();
            connection.close();
        }
        catch (IOException | TimeoutException e) {
            log.error("keycloak-to-rabbitmq ERROR on close", e);
        }
	}

	@Override
	public String getId() {
		return PROVIDER_ID;
	}
	
}
