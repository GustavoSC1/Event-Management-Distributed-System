package com.gustavo.paymentservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gustavo.paymentservice.dtos.PaymentRequestDTO;
import com.gustavo.paymentservice.services.PaymentService;

@RestController
@RequestMapping
public class PaymentController {
	
	@Autowired
	private PaymentService paymentService;
	
	@PostMapping("/payments/make")
	public ResponseEntity<String> makePayment(@Validated
            @RequestBody PaymentRequestDTO paymentRequestDto) {
		
		paymentService.makePayment(paymentRequestDto);
		
		return ResponseEntity.status(HttpStatus.CREATED).body("Payment made successfully!"); 
	}

}
