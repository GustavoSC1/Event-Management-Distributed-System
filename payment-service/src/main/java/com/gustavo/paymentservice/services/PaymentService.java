package com.gustavo.paymentservice.services;

import com.gustavo.paymentservice.dtos.PaymentRequestDTO;
import com.gustavo.paymentservice.dtos.PaymentResponseDTO;
import com.gustavo.paymentservice.entities.Payment;

public interface PaymentService {
	
	PaymentResponseDTO insert(Payment payment);
	
	void makePayment(PaymentRequestDTO paymentRequestDto);

}
