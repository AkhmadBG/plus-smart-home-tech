package ru.yandex.practicum.kafka.serializer;

import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.common.serialization.Deserializer;

public class GeneralAvroDeserializer implements Deserializer<SpecificRecordBase> {

    private final DecoderFactory decoderFactory = DecoderFactory.get();

    @Override
    public SpecificRecordBase deserialize(String topic, byte[] bytes) {

            return null;

    }

}