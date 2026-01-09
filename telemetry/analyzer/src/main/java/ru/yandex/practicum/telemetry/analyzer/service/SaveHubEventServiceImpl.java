package ru.yandex.practicum.telemetry.analyzer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.telemetry.analyzer.entity.Action;
import ru.yandex.practicum.telemetry.analyzer.entity.Condition;
import ru.yandex.practicum.telemetry.analyzer.entity.Scenario;
import ru.yandex.practicum.telemetry.analyzer.entity.Sensor;
import ru.yandex.practicum.telemetry.analyzer.mapper.Mapper;
import ru.yandex.practicum.telemetry.analyzer.repository.ActionRepository;
import ru.yandex.practicum.telemetry.analyzer.repository.ConditionRepository;
import ru.yandex.practicum.telemetry.analyzer.repository.ScenarioRepository;
import ru.yandex.practicum.telemetry.analyzer.repository.SensorRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SaveHubEventServiceImpl implements SaveHubEventService {

    private final ScenarioRepository scenarioRepository;
    private final SensorRepository sensorRepository;
    private final ActionRepository actionRepository;
    private final ConditionRepository conditionRepository;
    private final Mapper mapper;

    @Override
    public void saveDeviceAddedEventAvro(HubEventAvro hubEventAvro, DeviceAddedEventAvro deviceAddedEventAvro) {
        Sensor sensor = Sensor.builder()
                .id(deviceAddedEventAvro.getId())
                .hubId(hubEventAvro.getHubId())
                .build();
        sensorRepository.save(sensor);
    }

    @Override
    public void saveDeviceRemovedEventAvro(HubEventAvro hubEventAvro, DeviceRemovedEventAvro deviceRemovedEventAvro) {
        Optional<Sensor> sensor = sensorRepository.findByIdAndHubId(deviceRemovedEventAvro.getId(), hubEventAvro.getHubId());
        sensor.ifPresent(sensorRepository::delete);
    }

    @Override
    @Transactional
    public void saveScenarioAddedEventAvro(HubEventAvro hubEventAvro, ScenarioAddedEventAvro scenarioAddedEventAvro) {

        Scenario scenario = scenarioRepository
                .findByHubIdAndName(hubEventAvro.getHubId(), scenarioAddedEventAvro.getName())
                .orElseGet(() -> scenarioRepository.save(
                        Scenario.builder()
                                .hubId(hubEventAvro.getHubId())
                                .name(scenarioAddedEventAvro.getName())
                                .build()
                ));

        if (!scenarioAddedEventAvro.getConditions().isEmpty()) {
            List<String> conditionSensorIds = scenarioAddedEventAvro.getConditions().stream()
                    .map(ScenarioConditionAvro::getSensorId)
                    .toList();

            if (sensorRepository.existsByIdInAndHubId(conditionSensorIds, hubEventAvro.getHubId())) {
                conditionRepository.saveAll(
                        scenarioAddedEventAvro.getConditions().stream()
                                .map(condition -> Condition.builder()
                                        .scenario(scenario)
                                        .sensor(sensorRepository.findById(condition.getSensorId()).orElseThrow())
                                        .type(condition.getType())
                                        .operation(condition.getOperation())
                                        .value(setValue(condition.getValue()))
                                        .build())
                                .toList()
                );
            }
        }

        if (!scenarioAddedEventAvro.getActions().isEmpty()) {
            List<String> actionSensorIds = scenarioAddedEventAvro.getActions().stream()
                    .map(DeviceActionAvro::getSensorId)
                    .toList();

            if (sensorRepository.existsByIdInAndHubId(actionSensorIds, hubEventAvro.getHubId())) {
                actionRepository.saveAll(
                        scenarioAddedEventAvro.getActions().stream()
                                .map(action -> Action.builder()
                                        .scenario(scenario)
                                        .sensor(sensorRepository.findById(action.getSensorId()).orElseThrow())
                                        .type(action.getType())
                                        .value(
                                                action.getType() == ActionTypeAvro.SET_VALUE
                                                        ? setValue(action.getValue())
                                                        : null
                                        )
                                        .build())
                                .toList()
                );
            }
        }

    }

    @Override
    public void saveScenarioRemovedEventAvro(HubEventAvro hubEventAvro, ScenarioRemovedEventAvro scenarioRemovedEventAvro) {
        Optional<Scenario> scenario = scenarioRepository.findByHubIdAndName(hubEventAvro.getHubId(), scenarioRemovedEventAvro.getName());
        if (scenario.isPresent()) {
            scenarioRepository.deleteByHubIdAndName(hubEventAvro.getHubId(), scenarioRemovedEventAvro.getName());
        }
    }

    private Integer setValue(Object value) {
        if (value instanceof Integer) {
            return (Integer) value;
        } else {
            return (Boolean) value ? 1 : 0;
        }
    }

}