package ru.yandex.practicum.telemetry.collector.service.handler.sensor;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorEventAvro;
import ru.yandex.practicum.telemetry.collector.kafka.KafkaClient;
import ru.yandex.practicum.telemetry.collector.model.SensorEvent;
import ru.yandex.practicum.telemetry.collector.model.SensorEventType;
import ru.yandex.practicum.telemetry.collector.model.SwitchSensorEvent;

@Component(value = "SWITCH_SENSOR_EVENT")
public class SwitchSensorEventHandler extends BaseSensorEventHandler<SwitchSensorEvent> {

    public SwitchSensorEventHandler(KafkaClient client) {
        super(client);
    }

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.SWITCH_SENSOR_EVENT;
    }

    @Override
    public SensorEventAvro mapToAvro(SensorEvent event) {
        SwitchSensorEvent switchSensorEvent = (SwitchSensorEvent) event;
        SwitchSensorEventAvro switchSensorEventAvro = SwitchSensorEventAvro.newBuilder()
                .setState(switchSensorEvent.getState())
                .build();
        return SensorEventAvro.newBuilder()
                .setId(switchSensorEvent.getId())
                .setHubId(switchSensorEvent.getHubId())
                .setTimestamp(switchSensorEvent.getTimestamp())
                .setPayload(switchSensorEventAvro)
                .build();
    }

}