//package ru.yandex.practicum.telemetry.analyzer.service;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.apache.kafka.clients.consumer.ConsumerRecords;
//import org.apache.kafka.clients.consumer.KafkaConsumer;
//import org.apache.kafka.clients.consumer.OffsetAndMetadata;
//import org.apache.kafka.common.TopicPartition;
//import org.apache.kafka.common.errors.WakeupException;
//import org.springframework.stereotype.Component;
//import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
//
//import java.time.Duration;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class SnapshotProcessor implements Runnable {
//
//    private final Duration CONSUME_ATTEMPT_TIMEOUT = Duration.ofMillis(1000);
//    private final List<String> TOPICS = List.of("telemetry.snapshots.v1");
//    private final Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();
//
//    private final KafkaConsumer<Void, SensorsSnapshotAvro> consumer;
//    private final SnapshotAnalyzer snapshotAnalyzer;
//
//    @Override
//    public void run() {
//        try {
//            Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));
//            consumer.subscribe(TOPICS);
//            while (true) {
//                ConsumerRecords<Void, SensorsSnapshotAvro> records = consumer.poll(CONSUME_ATTEMPT_TIMEOUT);
//                int count = 0;
//                for (ConsumerRecord<Void, SensorsSnapshotAvro> record : records) {
//                    SensorsSnapshotAvro event = record.value();
//                    log.info(event.toString());
//                    snapshotAnalyzer.analyze(event);
//                    manageOffsets(record, count, consumer);
//                    count++;
//                }
//                consumer.commitAsync();
//            }
//
//        } catch (WakeupException ignores) {
//        } catch (Exception e) {
//            log.error("Ошибка во время обработки событий от датчиков", e);
//        } finally {
//
//            try {
//                consumer.commitSync(currentOffsets);
//            } finally {
//                log.info("Закрываем консьюмер");
//                consumer.close();
//            }
//        }
//    }
//
//    private void manageOffsets(ConsumerRecord<Void, SensorsSnapshotAvro> record, int count, KafkaConsumer<Void, SensorsSnapshotAvro> consumer) {
//        currentOffsets.put(
//                new TopicPartition(record.topic(), record.partition()),
//                new OffsetAndMetadata(record.offset() + 1)
//        );
//
//        if (count % 10 == 0) {
//            consumer.commitAsync(currentOffsets, (offsets, exception) -> {
//                if (exception != null) {
//                    log.warn("Ошибка во время фиксации оффсетов: {}", offsets, exception);
//                }
//            });
//        }
//    }
//
//}

package ru.yandex.practicum.telemetry.analyzer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Duration;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class SnapshotProcessor implements Runnable {

    private static final Duration POLL_TIMEOUT = Duration.ofSeconds(1);
    private static final int COMMIT_BATCH_SIZE = 10;
    private static final List<String> TOPICS = List.of("telemetry.snapshots.v1");

    private final KafkaConsumer<Void, SensorsSnapshotAvro> consumer;
    private final SnapshotAnalyzer snapshotAnalyzer;

    private final Map<TopicPartition, OffsetAndMetadata> offsetsToCommit = new HashMap<>();
    private int processedCount = 0;

    @Override
    public void run() {
        try {
            consumer.subscribe(TOPICS);
            log.info("SnapshotProcessor started, subscribed to {}", TOPICS);

            while (!Thread.currentThread().isInterrupted()) {
                ConsumerRecords<Void, SensorsSnapshotAvro> records =
                        consumer.poll(POLL_TIMEOUT);

                for (ConsumerRecord<Void, SensorsSnapshotAvro> record : records) {
                    processRecord(record);
                }

                commitIfNeeded();
            }

        } catch (WakeupException e) {
            log.info("SnapshotProcessor wakeup signal received");
        } catch (Exception e) {
            log.error("Fatal error in SnapshotProcessor loop", e);
        } finally {
            shutdown();
        }
    }

    private void processRecord(ConsumerRecord<Void, SensorsSnapshotAvro> record) {
        SensorsSnapshotAvro snapshot = record.value();

        try {
            log.info(
                    "Processing snapshot: hubId={}, partition={}, offset={}",
                    snapshot.getHubId(),
                    record.partition(),
                    record.offset()
            );

            snapshotAnalyzer.analyze(snapshot);

            offsetsToCommit.put(
                    new TopicPartition(record.topic(), record.partition()),
                    new OffsetAndMetadata(record.offset() + 1)
            );

            processedCount++;

        } catch (Exception e) {
            log.error(
                    "Error while processing snapshot (hubId={}, partition={}, offset={})",
                    snapshot != null ? snapshot.getHubId() : null,
                    record.partition(),
                    record.offset(),
                    e
            );
        }
    }

    private void commitIfNeeded() {
        if (processedCount >= COMMIT_BATCH_SIZE && !offsetsToCommit.isEmpty()) {
            consumer.commitAsync(offsetsToCommit, (offsets, exception) -> {
                if (exception != null) {
                    log.warn("Failed to commit offsets {}", offsets, exception);
                } else {
                    log.debug("Offsets committed: {}", offsets);
                }
            });
            processedCount = 0;
        }
    }

    private void shutdown() {
        try {
            if (!offsetsToCommit.isEmpty()) {
                consumer.commitSync(offsetsToCommit);
                log.info("Final offsets committed: {}", offsetsToCommit);
            }
        } catch (Exception e) {
            log.warn("Error during final offset commit", e);
        } finally {
            consumer.close();
            log.info("SnapshotProcessor stopped");
        }
    }
}
