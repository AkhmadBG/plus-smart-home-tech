package ru.yandex.practicum.commerce.delivery.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.yandex.practicum.commerce.interactionapi.delivery.enums.DeliveryState;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "delivery", schema = "delivery")
public class Delivery {

    @Id
    @GeneratedValue
    private UUID deliveryId;

    private UUID orderId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "from_address_id")
    private Address fromAddress;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "to_address_id")
    private Address toAddress;

    @Enumerated(EnumType.STRING)
    private DeliveryState deliveryState;

}