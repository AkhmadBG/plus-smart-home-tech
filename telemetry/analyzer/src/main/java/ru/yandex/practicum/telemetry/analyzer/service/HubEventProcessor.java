package ru.yandex.practicum.telemetry.analyzer.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.telemetry.analyzer.config.KafkaConfig;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class HubEventProcessor implements Runnable {

    private final KafkaConsumer<Void, HubEventAvro> consumer;
    private final List<String> TOPICS;
    private final Duration CONSUME_ATTEMPT_TIMEOUT;
    private final HubEventAnalyzer hubEventAnalyzer;
    private final Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();

    public HubEventProcessor(KafkaConfig config, HubEventAnalyzer hubEventAnalyzer) {
        this.hubEventAnalyzer = hubEventAnalyzer;
        final KafkaConfig.ConsumerConfig consumerConfig = config.getConsumers().get("HubEventProcessor");
        this.consumer = new KafkaConsumer<>(consumerConfig.getProperties());
        this.TOPICS = consumerConfig.getTopics();
        this.CONSUME_ATTEMPT_TIMEOUT = consumerConfig.getPollTimeout();
    }

    @Override
    public void run() {
        try {
            Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));
            consumer.subscribe(TOPICS);
            while (true) {
                ConsumerRecords<Void, HubEventAvro> records = consumer.poll(CONSUME_ATTEMPT_TIMEOUT);
                int count = 0;
                for (ConsumerRecord<Void, HubEventAvro> record : records) {
                    HubEventAvro event = record.value();
                    hubEventAnalyzer.analyze(event);
                    manageOffsets(record, count, consumer);
                    count++;
                }
                consumer.commitAsync();
            }

        } catch (WakeupException ignores) {
        } catch (Exception e) {
            log.error("Ошибка во время обработки событий от датчиков", e);
        } finally {

            try {
                consumer.commitSync(currentOffsets);
            } finally {
                log.info("Закрываем консьюмер");
                consumer.close();
            }
        }
    }

    private void manageOffsets(ConsumerRecord<Void, HubEventAvro> record, int count, KafkaConsumer<Void, HubEventAvro> consumer) {
        currentOffsets.put(
                new TopicPartition(record.topic(), record.partition()),
                new OffsetAndMetadata(record.offset() + 1)
        );

        if (count % 10 == 0) {
            consumer.commitAsync(currentOffsets, (offsets, exception) -> {
                if (exception != null) {
                    log.warn("Ошибка во время фиксации оффсетов: {}", offsets, exception);
                }
            });
        }
    }

}