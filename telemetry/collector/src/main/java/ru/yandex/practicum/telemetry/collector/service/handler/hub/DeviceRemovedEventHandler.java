package ru.yandex.practicum.telemetry.collector.service.handler.hub;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.telemetry.collector.kafka.KafkaClient;
import ru.yandex.practicum.telemetry.collector.model.DeviceRemovedEvent;
import ru.yandex.practicum.telemetry.collector.model.HubEvent;
import ru.yandex.practicum.telemetry.collector.model.HubEventType;

@Component(value = "DEVICE_REMOVED_HUB_EVENT")
public class DeviceRemovedEventHandler extends BaseHubEventHandler<DeviceRemovedEvent> {

    public DeviceRemovedEventHandler(KafkaClient producer) {
        super(producer);
    }

    @Override
    public HubEventType getMessageType() {
        return HubEventType.DEVICE_REMOVED;
    }

    @Override
    public HubEventAvro mapToAvro(HubEvent event) {
        DeviceRemovedEvent deviceRemovedEvent = (DeviceRemovedEvent) event;
        DeviceRemovedEventAvro deviceRemovedEventAvro = DeviceRemovedEventAvro.newBuilder()
                .setId(deviceRemovedEvent.getId())
                .build();
        return HubEventAvro.newBuilder()
                .setHubId(deviceRemovedEvent.getHubId())
                .setTimestamp(deviceRemovedEvent.getTimestamp())
                .setPayload(deviceRemovedEventAvro)
                .build();
    }

}