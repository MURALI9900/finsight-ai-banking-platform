package com.finsight.audit.event;

import java.time.Instant;

public record BankingEvent(
        String eventId,
        String eventType,
        String entityId,
        String customerId,
        String description,
        Instant occurredAt
) {}
