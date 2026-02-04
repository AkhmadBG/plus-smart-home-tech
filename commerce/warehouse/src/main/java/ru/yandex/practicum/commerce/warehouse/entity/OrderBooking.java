package ru.yandex.practicum.commerce.warehouse.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bookings", schema = "warehouse")
public class OrderBooking {

    @Id
    @GeneratedValue
    private UUID bookingId;

    @NotNull
    private UUID orderId;

    private UUID deliveryId;

    @OneToMany(
            mappedBy = "orderBooking",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<BookingItem> products = new ArrayList<>();

}