package ru.yandex.practicum.commerce.interactionapi.util;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

@UtilityClass
public class AppConstant {

    public static final BigDecimal TAX_RATE = new BigDecimal("0.10");

    public static final BigDecimal BASE_DELIVERY_RATE = new BigDecimal("5.0");

}