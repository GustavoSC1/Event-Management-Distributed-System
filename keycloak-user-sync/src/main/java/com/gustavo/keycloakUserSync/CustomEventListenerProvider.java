package com.gustavo.keycloakUserSync;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.UserModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.AMQP.BasicProperties.Builder;
import com.rabbitmq.client.Channel;

public class CustomEventListenerProvider implements EventListenerProvider {
	
	private Logger log = LoggerFactory.getLogger(CustomEventListenerProvider.class);
	
	private KeycloakSession session;	
	private ObjectMapper objectMapper;
	private Channel channel;
	
	public CustomEventListenerProvider(Channel channel, KeycloakSession session) {
		this.session = session;
		this.objectMapper = new ObjectMapper();	
		this.channel = channel;		
	}

	@Override
	public void close() {
		
	}

	@Override
	public void onEvent(Event event) {
		
	}

	@Override
	public void onEvent(AdminEvent adminEvent, boolean includeRepresentation) {
		if("USER".equals(adminEvent.getResourceType().name())) {
			if ("CREATE".equals(adminEvent.getOperationType().name())) {
				log.info("USER CREATE: " + adminEvent.getRepresentation());
				
				String userId = adminEvent.getResourcePath().substring("users/".length());
	            UserModel userModel = session.users().getUserById(session.getContext().getRealm(), userId);
	            
	            UserEventDTO userEventDto = new UserEventDTO(userModel);
	    		userEventDto.setActionType("CREATE");
	    		
	    		publishUser(userEventDto);						
			
			} else if ("UPDATE".equals(adminEvent.getOperationType().name())) {
				log.info("USER UPDATE: " + adminEvent.getRepresentation());
				
				String userId = adminEvent.getResourcePath().substring("users/".length());
	            UserModel userModel = session.users().getUserById(session.getContext().getRealm(), userId);
	            
	            UserEventDTO userEventDto = new UserEventDTO(userModel);
	    		userEventDto.setActionType("UPDATE");
	    		
	    		publishUser(userEventDto);	
	    		
			} else if("DELETE".equals(adminEvent.getOperationType().name())) {
				String userId = adminEvent.getResourcePath().substring("users/".length());				
				log.info("USER DELETE: " + userId);
				
				UserEventDTO userEventDto = new UserEventDTO();
				userEventDto.setUserId(UUID.fromString(userId));
		    	userEventDto.setActionType("DELETE");
		    	
		    	publishUser(userEventDto);
			}
		}
	}
	
	private BasicProperties getMessageProps() {
		
		Map<String,Object> headers = new HashMap<>();
		headers.put("__TypeId__", UserEventDTO.class.getName());
		
		Builder propsBuilder = new AMQP.BasicProperties.Builder()
				.appId("Keycloak")
				.headers(headers)
				.contentType("application/json")
				.contentEncoding("UTF-8");
		return propsBuilder.build();
	}
	
	private void publishUser(UserEventDTO userEventDto) {		

		try {
			String message = objectMapper.writeValueAsString(userEventDto);			
			
			channel.basicPublish("users.v1.user_event", "users.v1.user_event.send_user.key", getMessageProps(), message.getBytes("UTF-8"));
			
			log.info("keycloak-to-rabbitmq SUCCESS sending message: {}", "users.v1.user_event.send_user.key");
		} catch (Exception ex) {
			log.error("keycloak-to-rabbitmq ERROR sending message: {}", "users.v1.user_event.send_user.key", ex);
		}
	}
}
