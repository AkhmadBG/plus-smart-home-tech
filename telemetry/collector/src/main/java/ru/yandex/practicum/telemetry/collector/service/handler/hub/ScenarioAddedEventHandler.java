package ru.yandex.practicum.telemetry.collector.service.handler.hub;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioAddedEventProto;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.telemetry.collector.kafka.KafkaClient;
import ru.yandex.practicum.telemetry.collector.model.HubEvent;
import ru.yandex.practicum.telemetry.collector.model.HubEventType;
import ru.yandex.practicum.telemetry.collector.model.ScenarioAddedEvent;

import java.time.Instant;

@Component(value = "SCENARIO_ADDED_HUB_EVENT")
public class ScenarioAddedEventHandler extends BaseHubEventHandler<ScenarioAddedEvent> {

    public ScenarioAddedEventHandler(KafkaClient client) {
        super(client);
    }

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.SCENARIO_ADDED;
    }

    @Override
    public HubEventAvro mapToAvro(HubEventProto event) {

        ScenarioAddedEventProto scenario = event.getScenarioAdded();

        ScenarioAddedEventAvro scenarioAvro = ScenarioAddedEventAvro.newBuilder()
                .setName(scenario.getName())
                .setConditions(
                        scenario.getConditionList().stream()
                                .map(condition -> {
                                    ScenarioConditionAvro.Builder builder =
                                            ScenarioConditionAvro.newBuilder()
                                                    .setSensorId(condition.getSensorId())
                                                    .setType(ConditionTypeAvro.valueOf(condition.getType().name()))
                                                    .setOperation(ConditionOperationAvro.valueOf(condition.getOperation().name()));

                                    switch (condition.getValueCase()) {
                                        case BOOL_VALUE ->
                                                builder.setValue(condition.getBoolValue() ? 1 : 0);
                                        case INT_VALUE ->
                                                builder.setValue(condition.getIntValue());
                                        case VALUE_NOT_SET ->
                                                throw new IllegalArgumentException("Condition value not set");
                                    }

                                    return builder.build();
                                })
                                .toList()
                )
                .setActions(
                        scenario.getActionList().stream()
                                .map(action -> {
                                    DeviceActionAvro.Builder builder =
                                            DeviceActionAvro.newBuilder()
                                                    .setSensorId(action.getSensorId())
                                                    .setType(ActionTypeAvro.valueOf(action.getType().name()));

                                    if (action.hasValue()) {
                                        builder.setValue(action.getValue());
                                    }

                                    return builder.build();
                                })
                                .toList()
                )
                .build();

        Instant instant = Instant.ofEpochSecond(
                event.getTimestamp().getSeconds(),
                event.getTimestamp().getNanos()
        );

        return HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(instant)
                .setPayload(scenarioAvro)
                .build();
    }


}