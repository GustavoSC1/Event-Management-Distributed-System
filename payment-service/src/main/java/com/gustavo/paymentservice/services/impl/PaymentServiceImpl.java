package com.gustavo.paymentservice.services.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
	
	Logger log = LogManager.getLogger(PaymentServiceImpl.class);
	
	@Autowired
	private PaymentRepository paymentRepository;	
	
	@Autowired
	private PaymentMadeProducer paymentMadeProducer;
	
	@Override
	public PaymentResponseDTO insert(Payment payment) {
		paymentRepository.save(payment);
		
		PaymentResponseDTO paymentResponseDto = new PaymentResponseDTO();
		BeanUtils.copyProperties(payment, paymentResponseDto);
		
		log.debug("POST paymentServiceImpl insert paymentId: {} saved", paymentResponseDto.getPaymentId());
        log.info("Payment saved successfully paymentId: {}", paymentResponseDto.getPaymentId());
		
		return paymentResponseDto;
	}
	
	@Override
	public void makePayment(PaymentRequestDTO paymentRequestDto) {
		
		Payment payment = paymentRepository.findByPaymentCode(paymentRequestDto.getPaymentCode());
		
		if(payment == null) {
			log.warn("Payment not found! Code: {}", paymentRequestDto.getPaymentCode());
			throw new ObjectNotFoundException("Error: Payment not found! Code: " + paymentRequestDto.getPaymentCode());
		}
		
		if(payment.isPaid() == true) {
			log.warn("This payment has already been made! Code: {}", paymentRequestDto.getPaymentCode());
			throw new BusinessException("This payment has already been made!");
		}
		LocalDateTime currentDate = LocalDateTime.now(ZoneId.of("UTC"));
		if(currentDate.isAfter(payment.getDueDate())) {
			log.warn("It is not possible to make the payment because the due date has passed! Code: {}", paymentRequestDto.getPaymentCode());
			throw new BusinessException("It is not possible to make the payment because the due date has passed!");
		}
		
		payment.setPaid(true);
		payment.setPaymentDate(LocalDateTime.now(ZoneId.of("UTC")));
		
		paymentRepository.save(payment);	
				
		PaymentMadeEventDTO paymentMadeEventDto = new PaymentMadeEventDTO();		
		BeanUtils.copyProperties(payment, paymentMadeEventDto);
		paymentMadeEventDto.setPaid(true);
		paymentMadeEventDto.setPaymentDate(LocalDateTime.now(ZoneId.of("UTC")));
		
		log.debug("POST paymentServiceImpl makePayment paymentId: {} saved", payment.getPaymentId());
        log.info("Payment made successfully paymentId: {}", payment.getPaymentId());
		
		paymentMadeProducer.produceUserEvent(paymentMadeEventDto);
	}
	
	@Override
	public PaymentResponseDTO getOnePayment(UUID paymentId) {
		
		Payment payment = findById(paymentId);      
		PaymentResponseDTO paymentResponseDto = new PaymentResponseDTO();      
		BeanUtils.copyProperties(payment, paymentResponseDto);   
		
		log.debug("GET paymentServiceImpl getOnePayment paymentId: {} found", paymentResponseDto.getPaymentId());
        log.info("Payment found successfully paymentId: {}", paymentResponseDto.getPaymentId());
		
		return paymentResponseDto; 
	}
	
	@Override
	public Page<PaymentResponseDTO> findByUser(UUID userId, Pageable pageable) {
		
		Page<Payment> paymentPage = paymentRepository.findAllByUserId(userId, pageable);
		
		Page<PaymentResponseDTO> paymentResponseDtoPage = paymentPage.map(obj -> {    
			PaymentResponseDTO paymentResponseDto = new PaymentResponseDTO();    
			BeanUtils.copyProperties(obj, paymentResponseDto);    
			return paymentResponseDto;});
		
		log.debug("GET paymentServiceImpl findByUser userId: {} found", userId);
        log.info("Payments found successfully userId: {}", userId);
		
		return paymentResponseDtoPage;
	}
	
	@Override
	public Payment findById(UUID paymentId) {
		
		Optional<Payment> paymentOptional = paymentRepository.findById(paymentId);
		
		Payment payment = paymentOptional.orElseThrow(() -> {
			log.warn("Payment not found! paymentId: {}", paymentId);
			return new ObjectNotFoundException("Error: Payment not found! Id: " + paymentId);
			});
		
		log.debug("GET paymentServiceImpl findById paymentId: {} found", paymentId);
		
		return payment;
	}
	
}
