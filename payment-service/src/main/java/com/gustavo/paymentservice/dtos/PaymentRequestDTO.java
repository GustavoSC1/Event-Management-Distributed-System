package com.gustavo.paymentservice.dtos;

import java.io.Serializable;

import jakarta.validation.constraints.NotNull;

public class PaymentRequestDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@NotNull(message="The Payment Code field is required")
	private String paymentCode;

	public PaymentRequestDTO() {

	}

	public PaymentRequestDTO(String paymentCode) {
		this.paymentCode = paymentCode;
	}

	public String getPaymentCode() {
		return paymentCode;
	}

	public void setPaymentCode(String paymentCode) {
		this.paymentCode = paymentCode;
	}

	@Override
	public String toString() {
		return "PaymentRequestDTO [paymentCode=" + paymentCode + "]";
	}

}
