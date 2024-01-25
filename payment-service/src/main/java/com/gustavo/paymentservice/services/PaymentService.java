package com.gustavo.paymentservice.services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.gustavo.paymentservice.dtos.PaymentRequestDTO;
import com.gustavo.paymentservice.dtos.PaymentResponseDTO;
import com.gustavo.paymentservice.entities.Payment;

public interface PaymentService {
	
	PaymentResponseDTO insert(Payment payment);
	
	void makePayment(PaymentRequestDTO paymentRequestDto);
	
	Page<PaymentResponseDTO> findByUser(UUID userId, Pageable pageable);
	
	PaymentResponseDTO getOnePayment(UUID paymentId);
	
	Payment findById(UUID paymentId);
}
