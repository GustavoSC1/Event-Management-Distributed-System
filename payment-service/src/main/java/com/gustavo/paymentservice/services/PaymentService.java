package com.gustavo.paymentservice.services;

import com.gustavo.paymentservice.dtos.PaymentRequestDTO;

public interface PaymentService {
	
	void makePayment(PaymentRequestDTO paymentRequestDto);

}
