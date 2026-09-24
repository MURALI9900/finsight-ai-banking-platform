package com.finsight.audit.event;

import com.finsight.audit.model.AuditEventEntity;
import com.finsight.audit.repository.AuditEventRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class BankingEventConsumer {
    private final AuditEventRepository repository;

    public BankingEventConsumer(AuditEventRepository repository) {
        this.repository = repository;
    }

    @KafkaListener(topics = "${finsight.kafka.topic}", groupId = "${finsight.kafka.group-id}")
    public void consume(BankingEvent event) {
        repository.save(new AuditEventEntity(
                event.eventId(),
                event.eventType(),
                event.entityId(),
                event.customerId(),
                event.description(),
                event.occurredAt(),
                Instant.now()
        ));
    }
}
