package com.gustavo.userservice.services;

import java.util.UUID;

import org.springframework.data.domain.Pageable;

import com.gustavo.userservice.dtos.clientsDtos.PageImplResponseDTO;
import com.gustavo.userservice.dtos.clientsDtos.TicketResponseDTO;

public interface UserTicketService {
	
	PageImplResponseDTO<TicketResponseDTO> findAllTicketsByUser(UUID userId, Pageable pageable);

}
