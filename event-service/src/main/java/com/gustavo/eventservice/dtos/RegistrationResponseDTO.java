package com.gustavo.eventservice.dtos;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public class RegistrationResponseDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private UUID ticketId;
	private LocalDateTime registrationDate;
	private Boolean isPaid;	
	private UserResponseDTO userResponseDto;
	
	public RegistrationResponseDTO() {

	}

	public RegistrationResponseDTO(UUID ticketId, LocalDateTime registrationDate, Boolean isPaid,
			UserResponseDTO userResponseDto) {
		this.ticketId = ticketId;
		this.registrationDate = registrationDate;
		this.isPaid = isPaid;
		this.userResponseDto = userResponseDto;
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

}
