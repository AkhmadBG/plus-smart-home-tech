package ru.yandex.practicum.telemetry.analyzer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.telemetry.analyzer.entity.Scenario;
import ru.yandex.practicum.telemetry.analyzer.entity.Sensor;
import ru.yandex.practicum.telemetry.analyzer.repository.ScenarioRepository;
import ru.yandex.practicum.telemetry.analyzer.repository.SensorRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HubEventAnalyzer {

    private final ScenarioRepository scenarioRepository;
    private final SensorRepository sensorRepository;

    public void analyze(HubEventAvro event) {
        Object payload = event.getPayload();

        if (payload instanceof DeviceAddedEventAvro deviceAddedEventAvro) {
            if (!sensorRepository.existsByIdInAndHubId(List.of(deviceAddedEventAvro.getId()), event.getHubId())) {
                sensorRepository.save(new Sensor(deviceAddedEventAvro.getId(), event.getHubId()));
            }
        }

        if (payload instanceof DeviceRemovedEventAvro deviceRemovedEventAvro) {
            if (sensorRepository.existsByIdInAndHubId(List.of(deviceRemovedEventAvro.getId()), event.getHubId())) {
                sensorRepository.deleteById(deviceRemovedEventAvro.getId());
            }
        }

        if (payload instanceof ScenarioAddedEventAvro scenarioAddedEventAvro) {
            scenarioRepository.findByHubIdAndName(event.getHubId(), scenarioAddedEventAvro.getName())
                    .orElseGet(() -> {
                        Scenario scenario = Scenario.builder()
                                .hubId(event.getHubId())
                                .name(scenarioAddedEventAvro.getName())
                                .build();
                        return scenarioRepository.save(scenario);
                    });
        }

        if (payload instanceof ScenarioRemovedEventAvro scenarioRemovedEventAvro) {
            scenarioRepository.findByHubIdAndName(event.getHubId(), scenarioRemovedEventAvro.getName())
                    .ifPresent(scenarioRepository::delete);

        }
    }

}