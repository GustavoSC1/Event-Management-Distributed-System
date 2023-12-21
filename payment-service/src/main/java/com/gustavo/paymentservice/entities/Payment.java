package com.gustavo.paymentservice.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "TB_PAYMENT")
public class Payment implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID paymentId;
    
    @Column(nullable = false)
    private String paymentCode;
    
    @Column(nullable = false)
    private UUID ticketId;
    
    @Column(nullable = false)
    private UUID userId;
    
    @Column(nullable = false)
    private String details;
    
    @Column(nullable = false)
    private Double amount;
    
    @Column(nullable = false)
    private boolean isPaid;
    
    private LocalDateTime paymentDate;

	public Payment() {

	}

	public Payment(UUID paymentId, String paymentCode, UUID ticketId, UUID userId, String details, Double amount, boolean isPaid,
			LocalDateTime paymentDate) {
		this.paymentId = paymentId;
		this.paymentCode = paymentCode;
		this.ticketId = ticketId;
		this.userId = userId;
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
	public int hashCode() {
		return Objects.hash(amount, details, isPaid, paymentCode, paymentDate, paymentId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Payment other = (Payment) obj;
		return Objects.equals(amount, other.amount) && Objects.equals(details, other.details) && isPaid == other.isPaid
				&& Objects.equals(paymentCode, other.paymentCode) && Objects.equals(paymentDate, other.paymentDate)
				&& Objects.equals(paymentId, other.paymentId);
	}

}
