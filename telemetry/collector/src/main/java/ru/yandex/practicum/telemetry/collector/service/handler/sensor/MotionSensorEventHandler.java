package ru.yandex.practicum.telemetry.collector.service.handler.sensor;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.telemetry.collector.kafka.KafkaClient;
import ru.yandex.practicum.telemetry.collector.model.MotionSensorEvent;
import ru.yandex.practicum.telemetry.collector.model.SensorEvent;
import ru.yandex.practicum.telemetry.collector.model.SensorEventType;

@Component(value = "MOTION_SENSOR_EVENT")
public class MotionSensorEventHandler extends BaseSensorEventHandler<MotionSensorEvent> {

    public MotionSensorEventHandler(KafkaClient client) {
        super(client);
    }

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.MOTION_SENSOR_EVENT;
    }

    @Override
    public SensorEventAvro mapToAvro(SensorEvent event) {
        MotionSensorEvent motionSensorEvent = (MotionSensorEvent) event;
        MotionSensorEventAvro motionSensorEventAvro = MotionSensorEventAvro.newBuilder()
                .setLinkQuality(motionSensorEvent.getLinkQuality())
                .setMotion(motionSensorEvent.getMotion())
                .setVoltage(motionSensorEvent.getVoltage())
                .build();
        return SensorEventAvro.newBuilder()
                .setId(motionSensorEvent.getId())
                .setHubId(motionSensorEvent.getHubId())
                .setTimestamp(motionSensorEvent.getTimestamp())
                .setPayload(motionSensorEventAvro)
                .build();
    }

}