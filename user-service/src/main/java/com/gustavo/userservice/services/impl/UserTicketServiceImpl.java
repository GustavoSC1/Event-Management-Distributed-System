package com.gustavo.userservice.services.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.gustavo.userservice.clients.TicketClient;
import com.gustavo.userservice.dtos.clientsDtos.PageImplResponseDTO;
import com.gustavo.userservice.dtos.clientsDtos.TicketResponseDTO;
import com.gustavo.userservice.services.UserService;
import com.gustavo.userservice.services.UserTicketService;

@Service
public class UserTicketServiceImpl implements UserTicketService {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private TicketClient ticketClient;
	
	@Override
	public PageImplResponseDTO<TicketResponseDTO> findAllTicketsByUser(UUID userId, Pageable pageable) {

		userService.findById(userId);

		return ticketClient.findAllTicketsByUser(userId, pageable);		
	}

}
