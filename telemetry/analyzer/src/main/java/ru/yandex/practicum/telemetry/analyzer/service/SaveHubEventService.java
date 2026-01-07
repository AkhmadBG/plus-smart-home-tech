package ru.yandex.practicum.telemetry.analyzer.service;

import ru.yandex.practicum.kafka.telemetry.event.*;

public interface SaveHubEventService {

    void saveDeviceAddedEventAvro(HubEventAvro hubEventAvro, DeviceAddedEventAvro deviceAddedEventAvro);

    void saveDeviceRemovedEventAvro(HubEventAvro hubEventAvro, DeviceRemovedEventAvro deviceRemovedEventAvro);

    void saveScenarioAddedEventAvro(HubEventAvro hubEventAvro, ScenarioAddedEventAvro scenarioAddedEventAvro);

    void saveScenarioRemovedEventAvro(HubEventAvro hubEventAvro, ScenarioRemovedEventAvro scenarioRemovedEventAvro);

}