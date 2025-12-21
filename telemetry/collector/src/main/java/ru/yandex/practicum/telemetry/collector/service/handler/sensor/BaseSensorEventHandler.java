package ru.yandex.practicum.telemetry.collector.service.handler.sensor;

import lombok.Getter;
import lombok.Setter;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.telemetry.collector.kafka.KafkaClient;
import ru.yandex.practicum.telemetry.collector.kafka.KafkaTopics;
import ru.yandex.practicum.telemetry.collector.model.SensorEvent;
import ru.yandex.practicum.telemetry.collector.model.SensorEventType;
import ru.yandex.practicum.telemetry.collector.service.handler.SensorEventHandler;

@Getter
@Setter
public abstract class BaseSensorEventHandler<T extends SensorEvent> implements SensorEventHandler {

    private final KafkaClient client;

    public BaseSensorEventHandler(KafkaClient client) {
        this.client = client;
    }

    public abstract SensorEventProto.PayloadCase getMessageType();

    public abstract SensorEventAvro mapToAvro(SensorEventProto event);

    @Override
    public void handle(SensorEventProto event) {
        SensorEventAvro sensorEventAvro = mapToAvro(event);
        Producer<String, SpecificRecordBase> producer = client.getProducer();
        producer.send(new ProducerRecord<>(KafkaTopics.SENSOR_TOPIC, sensorEventAvro));
    }

}