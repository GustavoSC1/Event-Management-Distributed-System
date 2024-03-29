package com.gustavo.paymentservice.dtos.rabbitmqDtos;

import java.time.LocalDateTime;
import java.util.UUID;

public class PaymentMadeEventDTO {
	
	private String eventName;
	private String paymentCode;
	private UUID ticketId;
	private UUID userId;
	private boolean isPaid;
	private LocalDateTime paymentDate;
	
	public PaymentMadeEventDTO() {

	}
	
	public PaymentMadeEventDTO(String eventName, String paymentCode, UUID ticketId, UUID userId, boolean isPaid,
			LocalDateTime paymentDate) {
		this.eventName = eventName;
		this.paymentCode = paymentCode;
		this.ticketId = ticketId;
		this.userId = userId;
		this.isPaid = isPaid;
		this.paymentDate = paymentDate;
	}
	
	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getPaymentCode() {
		return paymentCode;
	}

	public void setPaymentCode(String paymentCode) {
		this.paymentCode = paymentCode;
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

	public boolean isPaid() {
		return isPaid;
	}

	public void setPaid(boolean isPaid) {
		this.isPaid = isPaid;
	}

	public LocalDateTime getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(LocalDateTime paymentDate) {
		this.paymentDate = paymentDate;
	}

	@Override
	public String toString() {
		return "PaymentMadeEventDTO [eventName=" + eventName + ", paymentCode=" + paymentCode + ", ticketId=" + ticketId
				+ ", userId=" + userId + ", isPaid=" + isPaid + ", paymentDate=" + paymentDate + "]";
	}
	
}
