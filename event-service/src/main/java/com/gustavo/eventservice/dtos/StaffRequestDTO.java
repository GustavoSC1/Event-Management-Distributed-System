package com.gustavo.eventservice.dtos;

import java.io.Serializable;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public class StaffRequestDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@NotNull(message="The User ID field is required")
	private UUID userId;
	
	public StaffRequestDTO() {

	}

	public StaffRequestDTO(UUID userId) {		
		this.userId = userId;
	}

	public UUID getUserId() {
		return userId;
	}

	public void setUserId(UUID userId) {
		this.userId = userId;
	}

}
