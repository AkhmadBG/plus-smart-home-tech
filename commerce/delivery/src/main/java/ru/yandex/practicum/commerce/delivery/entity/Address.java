package ru.yandex.practicum.commerce.delivery.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "addresses", schema = "delivery")
public class Address {

    @Id
    @GeneratedValue
    private UUID addressId;

    private String country;

    private String city;

    private String street;

    private String house;

    private String flat;

}