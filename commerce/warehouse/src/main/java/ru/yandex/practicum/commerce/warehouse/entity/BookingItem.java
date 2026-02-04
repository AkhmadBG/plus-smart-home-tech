package ru.yandex.practicum.commerce.warehouse.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "items", schema = "warehouse")
public class BookingItem {

    @Id
    @GeneratedValue
    private UUID itemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private OrderBooking orderBooking;

    @NotNull
    private UUID productId;

    @NotNull
    @Min(0)
    private Integer quantity;

}