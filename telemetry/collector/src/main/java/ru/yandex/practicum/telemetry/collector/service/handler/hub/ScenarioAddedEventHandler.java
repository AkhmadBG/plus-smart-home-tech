package ru.yandex.practicum.telemetry.collector.service.handler.hub;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.telemetry.collector.kafka.KafkaClient;
import ru.yandex.practicum.telemetry.collector.model.HubEvent;
import ru.yandex.practicum.telemetry.collector.model.HubEventType;
import ru.yandex.practicum.telemetry.collector.model.ScenarioAddedEvent;

@Component(value = "SCENARIO_ADDED_HUB_EVENT")
public class ScenarioAddedEventHandler extends BaseHubEventHandler<ScenarioAddedEvent> {

    public ScenarioAddedEventHandler(KafkaClient client) {
        super(client);
    }

    @Override
    public HubEventType getMessageType() {
        return HubEventType.SCENARIO_ADDED;
    }

    @Override
    public HubEventAvro mapToAvro(HubEvent event) {
        ScenarioAddedEvent scenarioAddedEvent = (ScenarioAddedEvent) event;
        ScenarioAddedEventAvro scenarioAddedEventAvro = ScenarioAddedEventAvro.newBuilder()
                .setName(scenarioAddedEvent.getName())
                .setConditions(scenarioAddedEvent.getConditions().stream()
                        .map(scenarioCondition -> ScenarioConditionAvro.newBuilder()
                                .setSensorId(scenarioCondition.getSensorId())
                                .setType(ConditionTypeAvro.valueOf(scenarioCondition.getType().name()))
                                .setOperation(ConditionOperationAvro.valueOf(scenarioCondition.getOperation().name()))
                                .setValue(scenarioCondition.getValue())
                                .build())
                        .toList())
                .setActions(scenarioAddedEvent.getActions().stream()
                        .map(deviceAction -> DeviceActionAvro.newBuilder()
                                .setSensorId(deviceAction.getSensorId())
                                .setType(ActionTypeAvro.valueOf(deviceAction.getType()))
                                .setValue(deviceAction.getValue())
                                .build())
                        .toList())
                .build();
        return HubEventAvro.newBuilder()
                .setHubId(scenarioAddedEvent.getHubId())
                .setTimestamp(scenarioAddedEvent.getTimestamp())
                .setPayload(scenarioAddedEventAvro)
                .build();
    }

}