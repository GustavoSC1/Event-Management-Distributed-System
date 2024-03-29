package com.gustavo.paymentservice.dtos;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentResponseDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private UUID paymentId;
	private String paymentCode;
	private UUID ticketId;
	private String eventName;
	private Double amount;
	private LocalDateTime paymentRequestDate;
	private LocalDateTime dueDate;
	private boolean isPaid;
	private LocalDateTime paymentDate;
	
	public PaymentResponseDTO() {

	}

	public PaymentResponseDTO(UUID paymentId, String paymentCode, UUID ticketId, String eventName, Double amount,
			LocalDateTime paymentRequestDate, LocalDateTime dueDate, boolean isPaid, LocalDateTime paymentDate) {
		this.paymentId = paymentId;
		this.paymentCode = paymentCode;
		this.ticketId = ticketId;
		this.eventName = eventName;
		this.amount = amount;
		this.paymentRequestDate = paymentRequestDate;
		this.dueDate = dueDate;
		this.isPaid = isPaid;
		this.paymentDate = paymentDate;
	}

	public UUID getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(UUID paymentId) {
		this.paymentId = paymentId;
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
	
	public LocalDateTime getPaymentRequestDate() {
		return paymentRequestDate;
	}

	public void setPaymentRequestDate(LocalDateTime paymentRequestDate) {
		this.paymentRequestDate = paymentRequestDate;
	}

	public LocalDateTime getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDateTime dueDate) {
		this.dueDate = dueDate;
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

}
