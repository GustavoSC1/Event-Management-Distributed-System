package com.gustavo.paymentservice.controllers;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/payments")
@Tag(name = "Payment endpoint")
public class PaymentController {
	
	Logger log = LogManager.getLogger(PaymentController.class);
	
	@Autowired
	private PaymentService paymentService;
	
	@Operation(summary = "Make payment")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Payment made successfully"),
			@ApiResponse(responseCode = "400", description = "This request can be processed"),
			@ApiResponse(responseCode = "403", description = "You are not allowed to make this request"),
			@ApiResponse(responseCode = "422", description = "Data validation error")
	})
	@PostMapping("/make")
	public ResponseEntity<String> makePayment(@Validated
            @RequestBody PaymentRequestDTO paymentRequestDto) {
		log.debug("POST paymentController makePayment paymentRequestDto received {}", paymentRequestDto.toString());
		paymentService.makePayment(paymentRequestDto);
		
		return ResponseEntity.status(HttpStatus.CREATED).body("Payment made successfully!"); 
	}
	
	@Operation(summary = "Obtains a payment details by id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Payment found successfully"),
			@ApiResponse(responseCode = "403", description = "You are not allowed to make this request"),
			@ApiResponse(responseCode = "404", description = "Could not find the requested data")
	})
	@GetMapping("/{paymentId}")
	public ResponseEntity<PaymentResponseDTO> getOnePayment(@PathVariable UUID paymentId) {
		log.debug("GET paymentController getOnePayment paymentId: {} received", paymentId);
		PaymentResponseDTO paymentResponseDto = paymentService.getOnePayment(paymentId);      
		
		return ResponseEntity.status(HttpStatus.OK).body(paymentResponseDto);
	}
	
	@Operation(summary = "Find payments by user")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Payments found successfully"),
			@ApiResponse(responseCode = "403", description = "You are not allowed to make this request")
	})
	@GetMapping("/users/{userId}")
	public ResponseEntity<Page<PaymentResponseDTO>> findByUser(
			@PathVariable(value = "userId") UUID userId,
            @PageableDefault(page = 0, size = 10, sort = "paymentRequestDate", direction = Sort.Direction.DESC)
            Pageable pageable) {
		log.debug("GET paymentController findByUser userId: {} received", userId);
		Page<PaymentResponseDTO> paymentResponseDtoPage = paymentService.findByUser(userId, pageable);
		
		return ResponseEntity.status(HttpStatus.OK).body(paymentResponseDtoPage);
	}
	
}
