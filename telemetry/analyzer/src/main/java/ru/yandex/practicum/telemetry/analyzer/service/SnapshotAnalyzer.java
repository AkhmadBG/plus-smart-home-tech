package ru.yandex.practicum.telemetry.analyzer.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.grpc.telemetry.event.ActionTypeProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.telemetry.analyzer.entity.Action;
import ru.yandex.practicum.telemetry.analyzer.entity.Condition;
import ru.yandex.practicum.telemetry.analyzer.entity.Scenario;
import ru.yandex.practicum.telemetry.analyzer.repository.ScenarioRepository;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SnapshotAnalyzer {

    private final ScenarioRepository scenarioRepository;

    @Getter
    @GrpcClient("hub-router")
    private HubRouterControllerGrpc.HubRouterControllerBlockingStub hubRouterClient;

    @Transactional
    public void analyze(SensorsSnapshotAvro event) {
        log.info("Start to analyze sensors snapshot");
        List<Scenario> scenarios = scenarioRepository.findByHubId(event.getHubId());
        Map<String, SensorStateAvro> sensorsState = event.getSensorsState();

        for (Scenario scenario : scenarios) {
            log.info("Start to analyze scenario {}", scenario.getName());
            boolean allConditionsMet = true;

            // Используем новые Condition
            List<Condition> conditions = scenario.getConditions(); // <-- нужно добавить геттер в Scenario или репозиторий
            for (Condition condition : conditions) {
                String sensorId = condition.getSensor().getId();
                SensorStateAvro sensorState = sensorsState.get(sensorId);

                if (sensorState == null) {
                    allConditionsMet = false;
                    break;
                }

                ConditionValueVisitor visitor = new ConditionValueVisitor(ConditionTypeAvro.valueOf(condition.getType().name()));
                Integer actualValue = SensorDataVisitorAdapter.accept(sensorState.getData(), visitor);

                if (!compareValues(actualValue, condition.getValue(), condition.getOperation())) {
                    allConditionsMet = false;
                    break;
                }
            }

            if (allConditionsMet) {
                log.info("All conditions met");
                executeScenarioActions(scenario);
            }
        }
    }

    private boolean compareValues(Integer actual, Integer expected, Enum<?> operation) {
        if (actual == null || expected == null) return false;

        return switch (operation.toString()) {
            case "EQUALS" -> actual.equals(expected);
            case "GREATER_THAN" -> actual > expected;
            case "LOWER_THAN" -> actual < expected;
            default -> false;
        };
    }

    private void executeScenarioActions(Scenario scenario) {
        log.info("Executing scenario {}", scenario.getId());
        List<Action> actions = scenario.getActions(); // <-- нужно добавить геттер в Scenario или репозиторий

        for (Action action : actions) {
            try {
                DeviceActionProto.Builder actionBuilder = DeviceActionProto.newBuilder()
                        .setSensorId(action.getSensor().getId())
                        .setType(ActionTypeProto.valueOf(action.getType().name()));

                if (action.getValue() != null) {
                    actionBuilder.setValue(action.getValue());
                }

                DeviceActionProto actionProto = actionBuilder.build();

                DeviceActionRequest request = DeviceActionRequest.newBuilder()
                        .setHubId(scenario.getHubId())
                        .setScenarioName(scenario.getName())
                        .setAction(actionProto)
                        .setTimestamp(
                                com.google.protobuf.Timestamp.newBuilder()
                                        .setSeconds(System.currentTimeMillis() / 1000)
                                        .setNanos((int) ((System.currentTimeMillis() % 1000) * 1_000_000))
                                        .build()
                        )
                        .build();

                hubRouterClient.handleDeviceAction(request);

            } catch (Exception e) {
                log.warn(
                        "Failed to execute action for scenario={}, sensor={}",
                        scenario.getName(),
                        action.getSensor().getId(),
                        e
                );
            }
        }
    }
}

//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class SnapshotAnalyzer {
//
//    private final ScenarioRepository scenarioRepository;
//
//    @GrpcClient("hub-router")
//    private HubRouterControllerGrpc.HubRouterControllerBlockingStub hubRouterClient;
//
//    @Transactional
//    public void analyze(SensorsSnapshotAvro event) {
//        String hubId = event.getHubId();
//
//        if (hubId == null) {
//            log.warn("Skip snapshot processing: hubId is null");
//            return;
//        }
//
//        log.info("Start analyzing snapshot for hubId={}", hubId);
//
//        Map<String, SensorStateAvro> sensorsState = event.getSensorsState();
//        if (sensorsState == null || sensorsState.isEmpty()) {
//            log.info("No sensors state in snapshot for hubId={}", hubId);
//            return;
//        }
//
//        // ВАЖНО: сценарии должны грузиться СРАЗУ с conditions + actions
//        List<Scenario> scenarios = scenarioRepository.findByHubIdWithConditionsAndActions(hubId);
//
//        if (scenarios.isEmpty()) {
//            log.info("No scenarios found for hubId={}", hubId);
//            return;
//        }
//
//        for (Scenario scenario : scenarios) {
//            log.info("Analyzing scenario '{}' for hubId={}", scenario.getName(), hubId);
//
//            if (!areAllConditionsMet(scenario, sensorsState)) {
//                log.info("Conditions NOT met for scenario '{}'", scenario.getName());
//                continue;
//            }
//
//            log.info("All conditions met for scenario '{}'", scenario.getName());
//            executeActions(scenario, hubId);
//        }
//    }
//
//    private boolean areAllConditionsMet(
//            Scenario scenario,
//            Map<String, SensorStateAvro> sensorsState
//    ) {
//        for (Condition condition : scenario.getConditions()) {
//            String sensorId = condition.getSensor().getId();
//            SensorStateAvro sensorState = sensorsState.get(sensorId);
//
//            if (sensorState == null) {
//                log.debug("No state for sensor {} (scenario {})", sensorId, scenario.getName());
//                return false;
//            }
//
//            Integer actualValue = extractActualValue(sensorState, condition);
//            if (actualValue == null) {
//                return false;
//            }
//
//            if (!compareValues(actualValue, condition.getValue(), condition.getOperation())) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    private Integer extractActualValue(SensorStateAvro sensorState, Condition condition) {
//        try {
//            ConditionTypeAvro type = ConditionTypeAvro.valueOf(condition.getType().name());
//            ConditionValueVisitor visitor = new ConditionValueVisitor(type);
//            return SensorDataVisitorAdapter.accept(sensorState.getData(), visitor);
//        } catch (Exception e) {
//            log.warn("Failed to extract value for condition {}", condition.getId(), e);
//            return null;
//        }
//    }
//
//    private boolean compareValues(Integer actual, Integer expected, Enum<?> operation) {
//        if (actual == null || expected == null) {
//            return false;
//        }
//
//        return switch (operation.name()) {
//            case "EQUALS" -> actual.equals(expected);
//            case "GREATER_THAN" -> actual > expected;
//            case "LOWER_THAN" -> actual < expected;
//            default -> {
//                log.warn("Unknown operation {}", operation);
//                yield false;
//            }
//        };
//    }
//
//    private void executeActions(Scenario scenario, String hubId) {
//        for (Action action : scenario.getActions()) {
//
//            ActionTypeProto actionType;
//            try {
//                actionType = ActionTypeProto.valueOf(action.getType().name());
//            } catch (IllegalArgumentException e) {
//                log.error(
//                        "Unknown action type {} for scenario {}",
//                        action.getType(),
//                        scenario.getName(),
//                        e
//                );
//                continue;
//            }
//
//            DeviceActionProto.Builder actionBuilder = DeviceActionProto.newBuilder()
//                    .setSensorId(action.getSensor().getId())
//                    .setType(actionType);
//
//            if (action.getValue() != null) {
//                actionBuilder.setValue(action.getValue());
//            }
//
//            DeviceActionRequest request = DeviceActionRequest.newBuilder()
//                    .setHubId(hubId)
//                    .setScenarioName(scenario.getName())
//                    .setAction(actionBuilder.build())
//                    .setTimestamp(currentTimestamp())
//                    .build();
//
//            log.info(
//                    "Sending action to HubRouter: hubId={}, scenario={}, sensor={}, type={}, value={}",
//                    hubId,
//                    scenario.getName(),
//                    action.getSensor().getId(),
//                    actionType,
//                    action.getValue()
//            );
//
//            try {
//                hubRouterClient.handleDeviceAction(request);
//            } catch (Exception e) {
//                log.error(
//                        "Failed to send action for scenario {}, sensor {}",
//                        scenario.getName(),
//                        action.getSensor().getId(),
//                        e
//                );
//            }
//        }
//    }
//
//    private com.google.protobuf.Timestamp currentTimestamp() {
//        long millis = System.currentTimeMillis();
//        return com.google.protobuf.Timestamp.newBuilder()
//                .setSeconds(millis / 1000)
//                .setNanos((int) ((millis % 1000) * 1_000_000))
//                .build();
//    }
//}
