package ru.yandex.practicum.telemetry.analyzer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.kafka.telemetry.event.ConditionOperationAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;
import ru.yandex.practicum.telemetry.analyzer.entity.Condition;

import java.util.Optional;

public interface ConditionRepository extends JpaRepository<Condition, Long> {
    Optional<Condition> findByTypeAndOperationAndValue(ConditionTypeAvro type, ConditionOperationAvro operation, Integer value);
}
