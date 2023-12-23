package com.gustavo.paymentservice.services.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gustavo.paymentservice.dtos.PaymentRequestDTO;
import com.gustavo.paymentservice.dtos.PaymentResponseDTO;
import com.gustavo.paymentservice.dtos.rabbitmqDtos.PaymentMadeEventDTO;
import com.gustavo.paymentservice.entities.Payment;
import com.gustavo.paymentservice.producers.PaymentMadeProducer;
import com.gustavo.paymentservice.repositories.PaymentRepository;
import com.gustavo.paymentservice.services.PaymentService;
import com.gustavo.paymentservice.services.exceptions.BusinessException;
import com.gustavo.paymentservice.services.exceptions.ObjectNotFoundException;

@Service
public class PaymentServiceImpl implements PaymentService {
	
	@Autowired
	private PaymentRepository paymentRepository;	
	
	@Autowired
	private PaymentMadeProducer paymentMadeProducer;
	
	@Override
	public PaymentResponseDTO insert(Payment payment) {
		paymentRepository.save(payment);
		
		PaymentResponseDTO paymentResponseDto = new PaymentResponseDTO();
		BeanUtils.copyProperties(payment, paymentResponseDto);
		
		return paymentResponseDto;
	}
	
	@Override
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
				
		PaymentMadeEventDTO paymentMadeEventDto = new PaymentMadeEventDTO();		
		BeanUtils.copyProperties(payment, paymentMadeEventDto);
		paymentMadeEventDto.setPaid(true);
		paymentMadeEventDto.setPaymentDate(LocalDateTime.now(ZoneId.of("UTC")));
		
		paymentMadeProducer.produceUserEvent(paymentMadeEventDto);
	}
	
}
