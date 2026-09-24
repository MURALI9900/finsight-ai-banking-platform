package com.finsight.audit.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "audit_events")
public class AuditEventEntity {
    @Id
    private String eventId;
    private String eventType;
    private String entityId;
    private String customerId;
    private String description;
    private Instant occurredAt;
    private Instant receivedAt;

    protected AuditEventEntity() {}

    public AuditEventEntity(String eventId, String eventType, String entityId,
                            String customerId, String description,
                            Instant occurredAt, Instant receivedAt) {
        this.eventId = eventId;
        this.eventType = eventType;
        this.entityId = entityId;
        this.customerId = customerId;
        this.description = description;
        this.occurredAt = occurredAt;
        this.receivedAt = receivedAt;
    }

    public String getEventId() { return eventId; }
    public String getEventType() { return eventType; }
    public String getEntityId() { return entityId; }
    public String getCustomerId() { return customerId; }
    public String getDescription() { return description; }
    public Instant getOccurredAt() { return occurredAt; }
    public Instant getReceivedAt() { return receivedAt; }
}
