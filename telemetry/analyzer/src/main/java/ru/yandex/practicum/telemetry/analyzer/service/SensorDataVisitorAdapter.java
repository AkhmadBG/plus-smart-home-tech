package ru.yandex.practicum.telemetry.analyzer.service;

import ru.yandex.practicum.kafka.telemetry.event.*;

public class SensorDataVisitorAdapter {

    public static Integer accept(
            Object sensorData,
            SensorValueVisitor visitor
    ) {
        if (sensorData instanceof ClimateSensorEventAvro climate) {
            return visitor.visit(climate);
        }
        if (sensorData instanceof LightSensorEventAvro light) {
            return visitor.visit(light);
        }
        if (sensorData instanceof MotionSensorEventAvro motion) {
            return visitor.visit(motion);
        }
        if (sensorData instanceof SwitchSensorEventAvro sw) {
            return visitor.visit(sw);
        }
        if (sensorData instanceof TemperatureSensorEventAvro temp) {
            return visitor.visit(temp);
        }
        throw new IllegalArgumentException(
                "Unsupported sensor data type: " + sensorData.getClass()
        );
    }
}
