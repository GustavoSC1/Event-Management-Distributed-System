package com.gustavo.paymentservice.dtos;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public class PaymentResponseDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private UUID paymentId;
	private String paymentCode;
	private UUID ticketId;
	private String details;
	private Double amount;
	private boolean isPaid;
	private LocalDateTime paymentDate;
	
	public PaymentResponseDTO() {

	}

	public PaymentResponseDTO(UUID paymentId, String paymentCode, UUID ticketId, String details, Double amount,
			boolean isPaid, LocalDateTime paymentDate) {
		this.paymentId = paymentId;
		this.paymentCode = paymentCode;
		this.ticketId = ticketId;
		this.details = details;
		this.amount = amount;
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
