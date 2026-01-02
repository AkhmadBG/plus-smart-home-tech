package ru.yandex.practicum.telemetry.aggregator;

import ru.yandex.practicum.kafka.serializer.GeneralAvroDeserializer;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

public class SensorEventDeserializer
        extends GeneralAvroDeserializer<SensorEventAvro> {

    public SensorEventDeserializer() {
        super(SensorEventAvro.getClassSchema());
    }

}