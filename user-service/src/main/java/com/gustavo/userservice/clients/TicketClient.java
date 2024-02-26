package com.gustavo.userservice.clients;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.gustavo.userservice.dtos.clientsDtos.PageImplResponseDTO;
import com.gustavo.userservice.dtos.clientsDtos.TicketResponseDTO;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import reactor.core.publisher.Mono;

@Component
public class TicketClient {
	
	Logger log = LogManager.getLogger(TicketClient.class);
	
	@Autowired
	public WebClient.Builder webClientTickets;
	
	@CircuitBreaker(name = "ticketsByUserCB", fallbackMethod = "fallbackMethod")
	public Page<TicketResponseDTO> findAllTicketsByUser(UUID userId, Pageable pageable, String token) {
		
		log.debug("CLIENT ticketClient findAllTicketsByUser userId: {} received", userId);
		
		String uri = createUri(userId, pageable);

		Mono<PageImplResponseDTO<TicketResponseDTO>> ticketMono = webClientTickets.build()
			.get()
			.uri(uri)		
			.headers(h -> h.setBearerAuth(token))
			.retrieve()
			.bodyToMono(new ParameterizedTypeReference<PageImplResponseDTO<TicketResponseDTO>>() {});

		PageImplResponseDTO<TicketResponseDTO> ticketPage = ticketMono.block();
		
		log.debug("CLIENT ticketClient findAllTicketsByUser userId: {} found", userId);

		return ticketPage;
	}
	
	public Page<TicketResponseDTO> fallbackMethod(UUID userId, Throwable throwable) {
		log.error("CLIENT ticketClient fallbackMethod userId: {}, cause {}", userId, throwable.toString());
		List<TicketResponseDTO> ticketList = new ArrayList<>();
		return new PageImpl<>(ticketList);
	}
	
	public String createUri(UUID userId, Pageable pageable) {
		return "/tickets/users/" + userId + "/events"
	            + "?page=" + pageable.getPageNumber()
	            + "&size=" + pageable.getPageSize()
	            + "$sort=" + pageable.getSort().toString().replaceAll(": ", ",");
	}

}
