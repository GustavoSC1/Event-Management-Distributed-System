package com.gustavo.eventservice.dtos;

import java.io.Serializable;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public class TicketRequestDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@NotNull(message="The User ID field is required")
	private UUID userId;
	
	public TicketRequestDTO() {

	}

	public TicketRequestDTO(UUID userId) {		
		this.userId = userId;
	}

	public UUID getUserId() {
		return userId;
	}

	public void setUserId(UUID userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "TicketRequestDTO [userId=" + userId + "]";
	}
	
}
