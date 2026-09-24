package com.finsight.banking.event;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/banking/events")
public class KafkaEventController {

    private final BankingEventPublisher publisher;

    public KafkaEventController(BankingEventPublisher publisher) {
        this.publisher = publisher;
    }

    @PostMapping("/demo")
    public ResponseEntity<Void> publishDemoEvent(@Valid @RequestBody DemoEventRequest request) {
        publisher.publish(request.eventType(), request.entityId(), request.customerId(), request.description());
        return ResponseEntity.accepted().build();
    }

    public record DemoEventRequest(
            @NotBlank String eventType,
            @NotBlank String entityId,
            @NotBlank String customerId,
            @NotBlank String description
    ) {}
}
