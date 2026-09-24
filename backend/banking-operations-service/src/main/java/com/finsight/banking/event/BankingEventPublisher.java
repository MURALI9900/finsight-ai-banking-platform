package com.finsight.banking.event;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class BankingEventPublisher {

    private final KafkaTemplate<String, BankingEvent> kafkaTemplate;
    private final String topic;

    public BankingEventPublisher(
            KafkaTemplate<String, BankingEvent> kafkaTemplate,
            @Value("${finsight.kafka.topic}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public void publish(String eventType, String entityId, String customerId, String description) {
        BankingEvent event = new BankingEvent(
                UUID.randomUUID().toString(),
                eventType,
                entityId,
                customerId,
                description,
                Instant.now()
        );
        kafkaTemplate.send(topic, entityId, event);
    }
}
