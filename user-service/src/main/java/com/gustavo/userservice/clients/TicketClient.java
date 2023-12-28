package com.gustavo.userservice.clients;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.gustavo.userservice.dtos.clientsDtos.PageImplResponseDTO;
import com.gustavo.userservice.dtos.clientsDtos.TicketResponseDTO;

import reactor.core.publisher.Mono;

@Component
public class TicketClient {
	
	@Autowired
	public WebClient.Builder webClientTickets;
	
	public PageImplResponseDTO<TicketResponseDTO> findAllTicketsByUser(UUID userId, Pageable pageable) {

		String uri = createUri(userId, pageable);

		Mono<PageImplResponseDTO<TicketResponseDTO>> ticketMono = webClientTickets.build()
			.get()
			.uri(uri)			
			.retrieve()
			.bodyToMono(new ParameterizedTypeReference<PageImplResponseDTO<TicketResponseDTO>>() {});

		PageImplResponseDTO<TicketResponseDTO> ticketPage = ticketMono.block();

		return ticketPage;
	}
	
	public String createUri(UUID userId, Pageable pageable) {
		return "/tickets/users/" + userId + "/events"
	            + "?page=" + pageable.getPageNumber()
	            + "&size=" + pageable.getPageSize()
	            + "$sort=" + pageable.getSort().toString().replaceAll(": ", ",");
	}

}
