package com.gustavo.userservice.services.impl;

import java.util.UUID;

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
import com.gustavo.userservice.services.CurrentUserService;
import com.gustavo.userservice.services.UserService;
import com.gustavo.userservice.services.UserTicketService;

@Service
public class UserTicketServiceImpl implements UserTicketService {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CurrentUserService currentUserService;
	
	@Autowired
	private TicketClient ticketClient;
	
	@Override
	public Page<TicketResponseDTO> findAllTicketsByUser(UUID userId, Pageable pageable) {
		
		UUID userAuthenticatedId = currentUserService.getCurrentUser().getId();
		
		if(!userAuthenticatedId.equals(userId)) {
			throw new AccessDeniedException("Error: Access denied!");
		}
		
		userService.findById(userId);
				
		String token = "";
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication.getPrincipal() instanceof Jwt authToken){			
			token = authToken.getTokenValue();			
	    }
		return ticketClient.findAllTicketsByUser(userId, pageable, token);		
	}

}
