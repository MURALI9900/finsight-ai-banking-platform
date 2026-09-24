package com.finsight.ai.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class RagService {
    private final RagVectorStore vectorStore;

    private final List<RagDocument> documents = List.of(
        new RagDocument("POL-001", "Loan Repayment Policy",
            "Loan repayments may be rejected when the available account balance is insufficient. Officers should verify the repayment amount, available balance, loan status, and transaction reference."),
        new RagDocument("POL-002", "Overdue Loan Operations",
            "Loans overdue beyond 30 days should be reviewed by an authorized officer. The officer should verify outstanding amount, overdue days, customer status, and applicable recovery workflow."),
        new RagDocument("POL-003", "Failed Transaction Handling",
            "For a failed transaction, officers should review the failure reason and transaction timestamp. Core banking timeout failures should be checked against the core banking service status before retrying.")
    );

    public RagService(RagVectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public List<RagDocument> retrieve(String question) {
        List<RagDocument> semantic = vectorStore.search(question, 3);
        if (!semantic.isEmpty()) return semantic;

        String q = question.toLowerCase(Locale.ROOT);
        return documents.stream()
                .filter(d -> matches(d.content().toLowerCase(Locale.ROOT) + " " + d.title().toLowerCase(Locale.ROOT), q))
                .limit(3)
                .toList();
    }

    public void indexPolicies() {
        documents.forEach(vectorStore::upsert);
    }

    private boolean matches(String document, String question) {
        return question.contains("loan") && document.contains("loan")
            || question.contains("repayment") && document.contains("repayment")
            || question.contains("failed") && document.contains("failed")
            || question.contains("transaction") && document.contains("transaction")
            || question.contains("overdue") && document.contains("overdue")
            || question.contains("timeout") && document.contains("timeout");
    }
}