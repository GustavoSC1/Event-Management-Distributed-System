package com.gustavo.paymentservice.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gustavo.paymentservice.dtos.PaymentRequestDTO;
import com.gustavo.paymentservice.dtos.PaymentResponseDTO;
import com.gustavo.paymentservice.services.PaymentService;

@RestController
@RequestMapping("/payments")
public class PaymentController {
	
	@Autowired
	private PaymentService paymentService;
	
	@PostMapping("/make")
	public ResponseEntity<String> makePayment(@Validated
            @RequestBody PaymentRequestDTO paymentRequestDto) {
		
		paymentService.makePayment(paymentRequestDto);
		
		return ResponseEntity.status(HttpStatus.CREATED).body("Payment made successfully!"); 
	}
	
	
	@GetMapping("/{paymentId}")
	public ResponseEntity<PaymentResponseDTO> getOnePayment(@PathVariable UUID paymentId) {
		
		PaymentResponseDTO paymentResponseDto = paymentService.getOnePayment(paymentId);      
		
		return ResponseEntity.status(HttpStatus.OK).body(paymentResponseDto);
	}
	
	@GetMapping("/users/{userId}")
	public ResponseEntity<Page<PaymentResponseDTO>> findByUser(
			@PathVariable(value = "userId")
            UUID userId,
            @PageableDefault(page = 0, size = 10, sort = "paymentRequestDate", direction = Sort.Direction.DESC)
            Pageable pageable) {
		
		Page<PaymentResponseDTO> paymentResponseDtoPage = paymentService.findByUser(userId, pageable);
		
		return ResponseEntity.status(HttpStatus.OK).body(paymentResponseDtoPage);
	}
	
}
