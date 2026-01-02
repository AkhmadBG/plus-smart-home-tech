package ru.yandex.practicum.telemetry.aggregator;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class SensorsSnapshotService {

    private final Map<String, SensorsSnapshotAvro> sensorsSnapshots = new HashMap<>();

    public Optional<SensorsSnapshotAvro> updateState(SensorEventAvro event) {
        SensorsSnapshotAvro sensorsSnapshotAvro = sensorsSnapshots.get(event.getHubId());

        if (sensorsSnapshotAvro == null) {
            sensorsSnapshotAvro = new SensorsSnapshotAvro();
            sensorsSnapshotAvro.setHubId(event.getHubId());
            sensorsSnapshotAvro.setSensorsState(new HashMap<>());
            sensorsSnapshotAvro.setTimestamp(event.getTimestamp());
            sensorsSnapshots.put(event.getHubId(), sensorsSnapshotAvro);
        }

        if (sensorsSnapshotAvro.getSensorsState() == null) {
            sensorsSnapshotAvro.setSensorsState(new HashMap<>());
        }

        Map<String, SensorStateAvro> sensorsState = sensorsSnapshotAvro.getSensorsState();

        SensorStateAvro oldState = sensorsState.get(event.getId());
        if (oldState != null) {
            if (oldState.getTimestamp().isAfter(event.getTimestamp()) ||
                    oldState.getData().equals(event.getPayload())) {
                return Optional.empty(); // изменений нет
            }
        }

        SensorStateAvro newState = new SensorStateAvro();
        newState.setTimestamp(event.getTimestamp());
        newState.setData(event.getPayload());

        sensorsState.put(event.getId(), newState);
        sensorsSnapshotAvro.setTimestamp(event.getTimestamp());

        return Optional.of(sensorsSnapshotAvro);
    }

}