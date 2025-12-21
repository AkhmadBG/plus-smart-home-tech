package ru.yandex.practicum.telemetry.collector.service.handler;

import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.telemetry.collector.model.SensorEvent;
import ru.yandex.practicum.telemetry.collector.model.SensorEventType;

public interface SensorEventHandler {

    SensorEventProto.PayloadCase getMessageType();

    void handle(SensorEventProto event);

}