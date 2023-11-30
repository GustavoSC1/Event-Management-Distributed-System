package com.gustavo.eventservice.dtos;

import java.io.Serializable;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public class RegistrationRequestDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@NotNull(message="The Creation User ID field is required")
	private UUID userId;
	
	public RegistrationRequestDTO() {

	}

	public RegistrationRequestDTO(UUID userId) {		
		this.userId = userId;
	}

	public UUID getUserId() {
		return userId;
	}

	public void setUserId(UUID userId) {
		this.userId = userId;
	}

}
