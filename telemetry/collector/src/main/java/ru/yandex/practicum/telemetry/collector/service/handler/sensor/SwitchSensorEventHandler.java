package ru.yandex.practicum.telemetry.collector.service.handler.sensor;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SwitchSensorProto;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorEventAvro;
import ru.yandex.practicum.telemetry.collector.kafka.KafkaClient;
import ru.yandex.practicum.telemetry.collector.model.SwitchSensorEvent;

import java.time.Instant;

@Component(value = "SWITCH_SENSOR_EVENT")
public class SwitchSensorEventHandler extends BaseSensorEventHandler<SwitchSensorEvent> {

    public SwitchSensorEventHandler(KafkaClient client) {
        super(client);
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.SWITCH_SENSOR;
    }

    @Override
    public SensorEventAvro mapToAvro(SensorEventProto event) {

        SwitchSensorProto switchSensorEvent = event.getSwitchSensor();

        SwitchSensorEventAvro switchSensorEventAvro = SwitchSensorEventAvro.newBuilder()
                .setState(switchSensorEvent.getState())
                .build();

        Instant instant = Instant.ofEpochSecond(
                event.getTimestamp().getSeconds(),
                event.getTimestamp().getNanos());

        return SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(instant)
                .setPayload(switchSensorEventAvro)
                .build();
    }

}