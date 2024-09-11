package com.gustavo.userservice.services.impl;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.gustavo.userservice.clients.TicketClient;
import com.gustavo.userservice.dtos.clientsDtos.TicketResponseDTO;
import com.gustavo.userservice.services.UserTicketService;

@Service
public class UserTicketServiceImpl implements UserTicketService {
	
	Logger log = LogManager.getLogger(UserTicketServiceImpl.class);
			
	@Autowired
	private TicketClient ticketClient;
	
	@Override
	public Page<TicketResponseDTO> findAllTicketsByUser(UUID userId, Pageable pageable) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if(!authentication.getName().equals(userId.toString())) {
			log.warn("Access denied! userId: {}", userId);
			throw new AccessDeniedException("Error: Access denied!");
		}
		
		String token = "";		
		if(authentication.getPrincipal() instanceof Jwt authToken){			
			token = authToken.getTokenValue();			
	    }
		
		log.debug("GET userTicketServiceImpl findAllTicketsByUser userId: {} found", userId);
        log.info("Tickets found successfully userId: {}", userId);
        
		return ticketClient.findAllTicketsByUser(userId, pageable, token);		
	}

}
