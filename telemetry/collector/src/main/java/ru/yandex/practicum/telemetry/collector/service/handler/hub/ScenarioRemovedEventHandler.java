package ru.yandex.practicum.telemetry.collector.service.handler.hub;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;
import ru.yandex.practicum.telemetry.collector.kafka.KafkaClient;
import ru.yandex.practicum.telemetry.collector.model.*;

@Component(value = "SCENARIO_REMOVED_HUB_EVENT")
public class ScenarioRemovedEventHandler extends BaseHubEventHandler<ScenarioRemovedEvent> {

    public ScenarioRemovedEventHandler(KafkaClient client) {
        super(client);
    }

    @Override
    public HubEventType getMessageType() {
        return HubEventType.SCENARIO_REMOVED;
    }

    @Override
    public HubEventAvro mapToAvro(HubEvent event) {
        ScenarioRemovedEvent scenarioRemovedEvent = (ScenarioRemovedEvent) event;
        ScenarioRemovedEventAvro scenarioRemovedEventAvro = ScenarioRemovedEventAvro.newBuilder()
                .setName(scenarioRemovedEvent.getName())
                .build();
        return HubEventAvro.newBuilder()
                .setHubId(scenarioRemovedEvent.getHubId())
                .setTimestamp(scenarioRemovedEvent.getTimestamp())
                .setPayload(scenarioRemovedEventAvro)
                .build();
    }

}