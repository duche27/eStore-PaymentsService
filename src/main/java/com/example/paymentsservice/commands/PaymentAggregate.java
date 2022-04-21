package com.example.paymentsservice.commands;

import com.gui.estore.core.commands.CancelPaymentCommand;
import com.gui.estore.core.commands.ProcessPaymentCommand;
import com.gui.estore.core.events.PaymentCancelledEvent;
import com.gui.estore.core.events.PaymentProcessedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import java.util.Objects;

@Aggregate
public class PaymentAggregate {

    @AggregateIdentifier
    private String paymentId;
    private String orderId;
    private String status;

    public PaymentAggregate() { }

    @CommandHandler
    public PaymentAggregate(ProcessPaymentCommand processPaymentCommand) {

        // validaciones
        hasCommandNullFields(processPaymentCommand);

        // creamos evento
        PaymentProcessedEvent paymentProcessedEvent = PaymentProcessedEvent.builder()
                .paymentId(processPaymentCommand.getPaymentId())
                .orderId(processPaymentCommand.getOrderId())
                .build();

        // publicamos evento y mandamos al eventHandler y a SAGA
        AggregateLifecycle.apply(paymentProcessedEvent);
    }

    @EventSourcingHandler
    public void on(PaymentProcessedEvent paymentProcessedEvent) {
        this.paymentId = paymentProcessedEvent.getPaymentId();
        this.orderId = paymentProcessedEvent.getOrderId();
        this.status = "PROCESADO";
    }

    @CommandHandler
    public void handle(CancelPaymentCommand cancelPaymentCommand) {

        // creamos evento
        PaymentCancelledEvent paymentCancelledEvent = PaymentCancelledEvent.builder()
                .paymentId(cancelPaymentCommand.getPaymentId())
                .orderId(cancelPaymentCommand.getOrderId())
                .build();

        // publicamos evento y mandamos al eventHandler y a SAGA
        AggregateLifecycle.apply(paymentCancelledEvent);
    }

    @EventSourcingHandler
    public void on(PaymentCancelledEvent paymentCancelledEvent) {
        this.paymentId = paymentCancelledEvent.getPaymentId();
        this.orderId = paymentCancelledEvent.getOrderId();
        this.status = "CANCELADO";
    }

    private void hasCommandNullFields(ProcessPaymentCommand processPaymentCommand) {

        if (Objects.isNull(processPaymentCommand.getPaymentId()) ||
                Objects.isNull(processPaymentCommand.getOrderId()) ||
                Objects.isNull(processPaymentCommand.getPaymentDetails()))
            throw new IllegalArgumentException("ProcessPaymentCommand con campos nulos");
    }
}
