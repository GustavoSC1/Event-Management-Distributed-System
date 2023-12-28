package com.gustavo.userservice.dtos.clientsDtos;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TicketResponseDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private UUID ticketId;
	private LocalDateTime registrationDate;
	private Boolean isPaid;
	private LocalDateTime paymentDate;
	private EventResponseDTO eventResponseDto;
	
	public TicketResponseDTO() {

	}

	public TicketResponseDTO(UUID ticketId, LocalDateTime registrationDate, Boolean isPaid,
			LocalDateTime paymentDate, EventResponseDTO eventResponseDto) {
		this.ticketId = ticketId;
		this.registrationDate = registrationDate;
		this.isPaid = isPaid;
		this.paymentDate = paymentDate;
	}

	public UUID getTicketId() {
		return ticketId;
	}

	public void setTicketId(UUID ticketId) {
		this.ticketId = ticketId;
	}

	public LocalDateTime getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(LocalDateTime registrationDate) {
		this.registrationDate = registrationDate;
	}

	public Boolean getIsPaid() {
		return isPaid;
	}

	public void setIsPaid(Boolean isPaid) {
		this.isPaid = isPaid;
	}
	
	public LocalDateTime getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(LocalDateTime paymentDate) {
		this.paymentDate = paymentDate;
	}

	public EventResponseDTO getEventResponseDto() {
		return eventResponseDto;
	}

	public void setEventResponseDto(EventResponseDTO eventResponseDto) {
		this.eventResponseDto = eventResponseDto;
	}

}
