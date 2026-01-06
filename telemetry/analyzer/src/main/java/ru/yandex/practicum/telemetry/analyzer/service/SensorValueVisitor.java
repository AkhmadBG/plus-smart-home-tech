package ru.yandex.practicum.telemetry.analyzer.service;

import ru.yandex.practicum.kafka.telemetry.event.*;

public interface SensorValueVisitor {

    Integer visit(ClimateSensorEventAvro climate);

    Integer visit(LightSensorEventAvro light);

    Integer visit(MotionSensorEventAvro motion);

    Integer visit(SwitchSensorEventAvro sw);

    Integer visit(TemperatureSensorEventAvro temperature);

}