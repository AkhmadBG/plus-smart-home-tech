package ru.yandex.practicum.telemetry.analyzer.config;

import ru.yandex.practicum.kafka.serializer.GeneralAvroDeserializer;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

public class HubEventDeserializer extends GeneralAvroDeserializer<HubEventAvro> {

    public HubEventDeserializer() {
        super(HubEventAvro.getClassSchema());
    }

}