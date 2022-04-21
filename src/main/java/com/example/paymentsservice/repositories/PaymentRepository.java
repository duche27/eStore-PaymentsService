package com.example.paymentsservice.repositories;

import com.example.paymentsservice.model.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<PaymentEntity, String> {

    Optional<PaymentEntity> findByPaymentId(String paymentId);
}
