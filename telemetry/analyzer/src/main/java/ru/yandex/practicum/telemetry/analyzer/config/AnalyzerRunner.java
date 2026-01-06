package ru.yandex.practicum.telemetry.analyzer.config;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.telemetry.analyzer.service.HubEventProcessor;
import ru.yandex.practicum.telemetry.analyzer.service.SnapshotProcessor;

@Component
@AllArgsConstructor
public class AnalyzerRunner implements CommandLineRunner {

    final HubEventProcessor hubEventProcessor;
    final SnapshotProcessor snapshotProcessor;

    @Override
    public void run(String... args) throws Exception {
        Thread hubEventsThread = new Thread(hubEventProcessor);
        hubEventsThread.setName("HubEventHandlerThread");
        hubEventsThread.start();

        Thread snapshotsThread = new Thread(snapshotProcessor);
        snapshotsThread.setName("SnapshotHandlerThread");
        snapshotsThread.start();
    }

}