package com.gustavo.eventservice.dtos;

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
	private UserResponseDTO userResponseDto;
	private EventResponseDTO eventResponseDto;
	
	public TicketResponseDTO() {

	}

	public TicketResponseDTO(UUID ticketId, LocalDateTime registrationDate, Boolean isPaid,
			UserResponseDTO userResponseDto, EventResponseDTO eventResponseDto) {
		this.ticketId = ticketId;
		this.registrationDate = registrationDate;
		this.isPaid = isPaid;
		this.userResponseDto = userResponseDto;
		this.eventResponseDto = eventResponseDto;
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

	public UserResponseDTO getUserResponseDto() {
		return userResponseDto;
	}

	public void setUserResponseDto(UserResponseDTO userResponseDto) {
		this.userResponseDto = userResponseDto;
	}

	public EventResponseDTO getEventResponseDto() {
		return eventResponseDto;
	}

	public void setEventResponseDto(EventResponseDTO eventResponseDto) {
		this.eventResponseDto = eventResponseDto;
	}

}
