package ru.yandex.practicum.telemetry.analyzer.service;

import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.kafka.telemetry.event.*;

@RequiredArgsConstructor
public class ConditionValueVisitor implements SensorValueVisitor {

    private final ConditionTypeAvro conditionType;

    @Override
    public Integer visit(ClimateSensorEventAvro climate) {
        return switch (conditionType) {
            case TEMPERATURE -> climate.getTemperatureC();
            case CO2LEVEL -> climate.getCo2Level();
            case HUMIDITY -> climate.getHumidity();
            default -> null;
        };
    }

    @Override
    public Integer visit(LightSensorEventAvro light) {
        return switch (conditionType) {
            case LUMINOSITY -> light.getLuminosity();
            default -> null;
        };
    }

    @Override
    public Integer visit(MotionSensorEventAvro motion) {
        return switch (conditionType) {
            case MOTION -> motion.getMotion() ? 1 : 0;
            default -> null;
        };
    }

    @Override
    public Integer visit(SwitchSensorEventAvro sw) {
        return switch (conditionType) {
            case SWITCH -> sw.getState() ? 1 : 0;
            default -> null;
        };
    }

    @Override
    public Integer visit(TemperatureSensorEventAvro temperature) {
        return switch (conditionType) {
            case TEMPERATURE -> temperature.getTemperatureC();
            default -> null;
        };
    }
}
