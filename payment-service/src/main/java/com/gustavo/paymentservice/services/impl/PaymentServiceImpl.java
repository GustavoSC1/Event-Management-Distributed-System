package com.gustavo.paymentservice.services.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gustavo.paymentservice.dtos.PaymentRequestDTO;
import com.gustavo.paymentservice.entities.Payment;
import com.gustavo.paymentservice.repositories.PaymentRepository;
import com.gustavo.paymentservice.services.PaymentService;
import com.gustavo.paymentservice.services.exceptions.BusinessException;
import com.gustavo.paymentservice.services.exceptions.ObjectNotFoundException;

@Service
public class PaymentServiceImpl implements PaymentService {
	
	@Autowired
	private PaymentRepository paymentRepository;	
	
	public void makePayment(PaymentRequestDTO paymentRequestDto) {
		
		Payment payment = paymentRepository.findByPaymentCode(paymentRequestDto.getPaymentCode());
		
		if(payment == null) {
			throw new ObjectNotFoundException("Payment not found! Code: " + paymentRequestDto.getPaymentCode());
		}
		
		if(payment.isPaid() == true) {
			throw new BusinessException("This payment has already been made!");
		}
		
		payment.setPaid(true);
		payment.setPaymentDate(LocalDateTime.now(ZoneId.of("UTC")));
		
		paymentRepository.save(payment);	
	}

}
