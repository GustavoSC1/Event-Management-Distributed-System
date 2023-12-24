package com.gustavo.paymentservice.dtos.rabbitmqDtos;

import java.util.UUID;

public class PaymentEventDTO {

	private UUID ticketId;
	private UUID userId;
	private String eventName;
	private Double amount;
	
	public PaymentEventDTO() {

	}

	public PaymentEventDTO(UUID ticketId, UUID userId, String eventName, Double amount) {
		this.ticketId = ticketId;
		this.userId = userId;
		this.eventName = eventName;
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

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
}
