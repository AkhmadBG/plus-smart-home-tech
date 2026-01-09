package ru.yandex.practicum.telemetry.analyzer.config;

import ru.yandex.practicum.kafka.serializer.GeneralAvroDeserializer;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

public class SensorSnapshotDeserializer extends GeneralAvroDeserializer<SensorsSnapshotAvro> {

    public SensorSnapshotDeserializer() {
        super(SensorsSnapshotAvro.getClassSchema());
    }

}