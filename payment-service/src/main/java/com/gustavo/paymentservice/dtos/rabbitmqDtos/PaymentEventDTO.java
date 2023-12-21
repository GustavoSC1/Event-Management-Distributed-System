package com.gustavo.paymentservice.dtos.rabbitmqDtos;

import java.util.UUID;

public class PaymentEventDTO {

	private UUID ticketId;
	private UUID userId;
	private String details;
	private Double amount;
	
	public PaymentEventDTO() {

	}

	public PaymentEventDTO(UUID ticketId, UUID userId, String details, Double amount) {
		this.ticketId = ticketId;
		this.userId = userId;
		this.details = details;
		this.amount = amount;
	}

	public UUID getTicketId() {
		return ticketId;
	}

	public void setTicketId(UUID ticketId) {
		this.ticketId = ticketId;
	}

	public UUID getUserId() {
		return userId;
	}

	public void setUserId(UUID userId) {
		this.userId = userId;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
}
