package ru.yandex.practicum.telemetry.analyzer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.*;

@Service
@RequiredArgsConstructor
public class HubEventAnalyzer {

    private final SaveHubEventService saveHubEventService;

    public void analyze(HubEventAvro hubEventAvro) {
        Object payload = hubEventAvro.getPayload();

        if (payload instanceof DeviceAddedEventAvro deviceAddedEventAvro) {
            saveHubEventService.saveDeviceAddedEventAvro(hubEventAvro, deviceAddedEventAvro);
        }

        if (payload instanceof DeviceRemovedEventAvro deviceRemovedEventAvro) {
            saveHubEventService.saveDeviceRemovedEventAvro(hubEventAvro, deviceRemovedEventAvro);
        }

        if (payload instanceof ScenarioAddedEventAvro scenarioAddedEventAvro) {
            saveHubEventService.saveScenarioAddedEventAvro(hubEventAvro, scenarioAddedEventAvro);
        }

        if (payload instanceof ScenarioRemovedEventAvro scenarioRemovedEventAvro) {
            saveHubEventService.saveScenarioRemovedEventAvro(hubEventAvro, scenarioRemovedEventAvro);
        }
    }

}