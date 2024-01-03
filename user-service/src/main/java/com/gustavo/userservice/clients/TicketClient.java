package com.gustavo.userservice.clients;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
	
	@Autowired
	public WebClient.Builder webClientTickets;
	
	@CircuitBreaker(name = "ticketsByUserCB", fallbackMethod = "fallbackMethod")
	public Page<TicketResponseDTO> findAllTicketsByUser(UUID userId, Pageable pageable) {

		String uri = createUri(userId, pageable);

		Mono<PageImplResponseDTO<TicketResponseDTO>> ticketMono = webClientTickets.build()
			.get()
			.uri(uri)			
			.retrieve()
			.bodyToMono(new ParameterizedTypeReference<PageImplResponseDTO<TicketResponseDTO>>() {});

		PageImplResponseDTO<TicketResponseDTO> ticketPage = ticketMono.block();

		return ticketPage;
	}
	
	public Page<TicketResponseDTO> fallbackMethod(Throwable throwable) {

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
