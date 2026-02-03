package ru.yandex.practicum.commerce.payment.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.yandex.practicum.commerce.interactionapi.payment.enums.PaymentState;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payments", schema = "payment")
public class Payment {

    @Id
    @GeneratedValue
    private UUID paymentId;

    @NotNull
    private UUID orderId;

    private BigDecimal totalPayment;

    private BigDecimal deliveryTotal;

    private BigDecimal feeTotal;

    @Enumerated(EnumType.STRING)
    private PaymentState paymentState;

}