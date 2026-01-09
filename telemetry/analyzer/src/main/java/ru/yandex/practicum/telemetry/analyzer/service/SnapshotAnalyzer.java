package ru.yandex.practicum.telemetry.analyzer.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.telemetry.analyzer.entity.Condition;
import ru.yandex.practicum.telemetry.analyzer.entity.Scenario;
import ru.yandex.practicum.telemetry.analyzer.repository.ActionRepository;
import ru.yandex.practicum.telemetry.analyzer.repository.ConditionRepository;
import ru.yandex.practicum.telemetry.analyzer.repository.ScenarioRepository;

import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SnapshotAnalyzer {

    final HubRouterClient hubRouterClient;
    final ConditionRepository conditionRepository;
    final ActionRepository actionRepository;
    final ScenarioRepository scenarioRepository;

    public void analyze(SensorsSnapshotAvro snapshot) {
        log.info("Starting to analyze sensors snapshot, hubId={}", snapshot.getHubId());

        Map<String, SensorStateAvro> sensorState = snapshot.getSensorsState();

        scenarioRepository.findByHubId(snapshot.getHubId()).stream()
                .filter(scenario -> {
                    return (boolean) checkScenario(scenario, sensorState);
                })
                .forEach(this::sendAction);
    }

    private Boolean checkOperation(Condition condition, Integer value) {
        Integer conditionValue = condition.getValue();

        switch (condition.getOperation()) {
            case EQUALS -> {
                return Objects.equals(value, conditionValue);
            }
            case GREATER_THAN -> {
                return value > conditionValue;
            }
            case LOWER_THAN -> {
                return value < conditionValue;
            }
            default -> {
                return false;
            }
        }
    }

    private Boolean checkCondition(Condition condition, Map<String, SensorStateAvro> sensorState) {
        SensorStateAvro sensorStateAvro = sensorState.get(condition.getSensor().getId());

        if (sensorStateAvro == null) return false;

        switch (condition.getType()) {
            case SWITCH -> {
                SwitchSensorEventAvro switchSensor = (SwitchSensorEventAvro) sensorStateAvro.getData();
                return checkOperation(condition, switchSensor.getState() ? 1 : 0);
            }
            case MOTION -> {
                MotionSensorEventAvro motionSensor = (MotionSensorEventAvro) sensorStateAvro.getData();
                return checkOperation(condition, motionSensor.getMotion() ? 1 : 0);
            }
            case HUMIDITY -> {
                ClimateSensorEventAvro humiditySensor = (ClimateSensorEventAvro) sensorStateAvro.getData();
                return checkOperation(condition, humiditySensor.getHumidity());
            }
            case TEMPERATURE -> {
                ClimateSensorEventAvro temperatureSensor = (ClimateSensorEventAvro) sensorStateAvro.getData();
                return checkOperation(condition, temperatureSensor.getTemperatureC());
            }
            case LUMINOSITY -> {
                LightSensorEventAvro lightSensor = (LightSensorEventAvro) sensorStateAvro.getData();
                return checkOperation(condition, lightSensor.getLuminosity());
            }
            case CO2LEVEL -> {
                ClimateSensorEventAvro co2Sensor = (ClimateSensorEventAvro) sensorStateAvro.getData();
                return checkOperation(condition, co2Sensor.getCo2Level());
            }

            default -> {
                return false;
            }
        }
    }

    private Boolean checkScenario(Scenario scenario, Map<String, SensorStateAvro> sensorState) {
        return conditionRepository.findAllByScenario(scenario).stream()
                .allMatch(condition -> checkCondition(condition, sensorState));
    }

    private void sendAction(Scenario scenario) {
        var actions = actionRepository.findAllByScenario(scenario);

        actions.forEach(action -> {
            try {
                hubRouterClient.sendRequest(action);
            } catch (Exception e) {
                log.error(
                        "Failed to send action id={} for scenario id={}",
                        action.getId(),
                        scenario.getId(),
                        e
                );
            }
        });
    }

}