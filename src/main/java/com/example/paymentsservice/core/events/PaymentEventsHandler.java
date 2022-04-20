package com.example.paymentsservice.core.events;

import com.example.paymentsservice.model.PaymentEntity;
import com.example.paymentsservice.repositories.PaymentRepository;
import com.gui.estore.core.events.PaymentProcessedEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@ProcessingGroup("payment-group")
public class PaymentEventsHandler {

    private final PaymentRepository paymentRepository;

    public PaymentEventsHandler(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @EventHandler
    public void on(PaymentProcessedEvent paymentProcessedEvent) {

        PaymentEntity paymentEntity = new PaymentEntity();

        BeanUtils.copyProperties(paymentProcessedEvent, paymentEntity);

        try {
            paymentRepository.save(paymentEntity);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    // lanza la excepción controlada si no persiste paymentEntity
    // sin persistir nada, es transaccional
    // de aquí va a PaymentServiceEventHandler - después a PaymentErrorHandler - excepción controlada
    @ExceptionHandler(resultType = Exception.class)
    private void handle(Exception exception) throws Exception {
        throw exception;
    }

    @ExceptionHandler(resultType = IllegalArgumentException.class)
    private void handle(IllegalArgumentException exception) throws IllegalArgumentException {
//        throw IllegalArgumentException;
    }
}
