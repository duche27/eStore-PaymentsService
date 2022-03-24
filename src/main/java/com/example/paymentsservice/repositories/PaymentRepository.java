package com.example.paymentsservice.repositories;

import com.example.paymentsservice.model.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<PaymentEntity, String> {
    
}
