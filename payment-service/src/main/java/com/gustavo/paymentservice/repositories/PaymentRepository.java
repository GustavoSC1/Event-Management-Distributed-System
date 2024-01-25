package com.gustavo.paymentservice.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.gustavo.paymentservice.entities.Payment;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
	
	@Transactional(readOnly = true)
	Payment findByPaymentCode(String paymentCode);
	
	@Transactional(readOnly = true)
	Page<Payment> findAllByUserId(UUID userId, Pageable pageable);	
}
