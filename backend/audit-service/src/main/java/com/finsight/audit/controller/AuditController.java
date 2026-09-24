package com.finsight.audit.controller;

import com.finsight.audit.model.AuditEventEntity;
import com.finsight.audit.repository.AuditEventRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/audit")
public class AuditController {
    private final AuditEventRepository repository;

    public AuditController(AuditEventRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/events")
    public List<AuditEventEntity> events() {
        return repository.findAll();
    }
}
