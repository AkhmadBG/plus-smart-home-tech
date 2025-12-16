package ru.yandex.practicum.telemetry.collector.service.handler.sensor;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorEventAvro;
import ru.yandex.practicum.telemetry.collector.kafka.KafkaClient;
import ru.yandex.practicum.telemetry.collector.model.SensorEvent;
import ru.yandex.practicum.telemetry.collector.model.SensorEventType;
import ru.yandex.practicum.telemetry.collector.model.TemperatureSensorEvent;

@Component(value = "TEMPERATURE_SENSOR_EVENT")
public class TemperatureSensorEventHandler extends BaseSensorEventHandler<TemperatureSensorEvent> {

    public TemperatureSensorEventHandler(KafkaClient client) {
        super(client);
    }

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.TEMPERATURE_SENSOR_EVENT;
    }

    @Override
    public SensorEventAvro mapToAvro(SensorEvent event) {
        TemperatureSensorEvent temperatureSensorEvent = (TemperatureSensorEvent) event;
        TemperatureSensorEventAvro temperatureSensorEventAvro = TemperatureSensorEventAvro.newBuilder()
                .setTemperatureC(temperatureSensorEvent.getTemperatureC())
                .setTemperatureF(temperatureSensorEvent.getTemperatureF())
                .build();
        return SensorEventAvro.newBuilder()
                .setId(temperatureSensorEvent.getId())
                .setHubId(temperatureSensorEvent.getHubId())
                .setTimestamp(temperatureSensorEvent.getTimestamp())
                .setPayload(temperatureSensorEventAvro)
                .build();
    }

}