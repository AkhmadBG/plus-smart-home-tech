package ru.yandex.practicum.telemetry.collector.service.handler.hub;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.DeviceAddedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceTypeProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.telemetry.collector.kafka.KafkaClient;
import ru.yandex.practicum.telemetry.collector.model.DeviceAddedEvent;
import ru.yandex.practicum.telemetry.collector.model.HubEvent;
import ru.yandex.practicum.telemetry.collector.model.HubEventType;

import java.time.Instant;

@Component(value = "DEVICE_ADDED_HUB_EVENT")
public class DeviceAddedEventHandler extends BaseHubEventHandler<DeviceAddedEvent> {

    public DeviceAddedEventHandler(KafkaClient client) {
        super(client);
    }

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.DEVICE_ADDED;
    }

//    @Override
//    public HubEventAvro mapToAvro(HubEvent event) {
//        DeviceAddedEvent deviceAddedEvent = (DeviceAddedEvent) event;
//        DeviceAddedEventAvro deviceAddedEventAvro = DeviceAddedEventAvro.newBuilder()
//                .setId(deviceAddedEvent.getId())
//                .setType(DeviceTypeAvro.valueOf(deviceAddedEvent.getDeviceType().name()))
//                .build();
//        return HubEventAvro.newBuilder()
//                .setHubId(deviceAddedEvent.getHubId())
//                .setTimestamp(deviceAddedEvent.getTimestamp())
//                .setPayload(deviceAddedEventAvro)
//                .build();
//    }

    @Override
    public HubEventAvro mapToAvro(HubEventProto event) {

        DeviceAddedEventProto deviceAddedEvent = event.getDeviceAdded();

        DeviceAddedEventAvro deviceAddedEventAvro = DeviceAddedEventAvro.newBuilder()
                .setId(deviceAddedEvent.getId())
                .setType(DeviceTypeAvro.valueOf(deviceAddedEvent.getType().toString()))
                .build();

        Instant instant = Instant.ofEpochSecond(
                event.getTimestamp().getSeconds(),
                event.getTimestamp().getNanos());

        return HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(instant)
                .setPayload(deviceAddedEventAvro)
                .build();
    }

}