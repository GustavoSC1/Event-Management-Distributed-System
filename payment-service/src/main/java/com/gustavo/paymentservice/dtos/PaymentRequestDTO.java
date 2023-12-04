package com.gustavo.paymentservice.dtos;

import java.io.Serializable;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public class PaymentRequestDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@NotNull(message="The Payment Code field is required")
	private UUID paymentCode;

	public PaymentRequestDTO() {

	}

	public PaymentRequestDTO(UUID paymentCode) {
		this.paymentCode = paymentCode;
	}

	public UUID getPaymentCode() {
		return paymentCode;
	}

	public void setPaymentCode(UUID paymentCode) {
		this.paymentCode = paymentCode;
	}

}
