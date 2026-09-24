package com.finsight.ai.service;

import jakarta.annotation.PostConstruct;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class RagVectorStore {
    private final JdbcTemplate jdbc;
    private final EmbeddingClient embeddings;

    public RagVectorStore(JdbcTemplate jdbc, EmbeddingClient embeddings) {
        this.jdbc = jdbc;
        this.embeddings = embeddings;
    }

    @PostConstruct
    void initialize() {
        jdbc.execute("CREATE EXTENSION IF NOT EXISTS vector");
        jdbc.execute("CREATE TABLE IF NOT EXISTS rag_documents (" +
                "id VARCHAR(100) PRIMARY KEY, title VARCHAR(255) NOT NULL, content TEXT NOT NULL, " +
                "embedding vector(1536))");
    }

    public void upsert(RagDocument document) {
        List<Double> vector = embeddings.embed(document.content());
        if (vector.isEmpty()) return;
        String literal = vector.toString().replace("[", "[").replace("]", "]");
        jdbc.update("""
            INSERT INTO rag_documents(id,title,content,embedding)
            VALUES (?,?,?,?::vector)
            ON CONFLICT (id) DO UPDATE SET title=EXCLUDED.title,content=EXCLUDED.content,embedding=EXCLUDED.embedding
            """, document.id(), document.title(), document.content(), literal);
    }

    public List<RagDocument> search(String question, int limit) {
        List<Double> vector = embeddings.embed(question);
        if (vector.isEmpty()) return List.of();
        String literal = vector.toString();
        return jdbc.query("""
            SELECT id,title,content
            FROM rag_documents
            WHERE embedding IS NOT NULL
            ORDER BY embedding <=> ?::vector
            LIMIT ?
            """, (rs, rowNum) -> new RagDocument(
                rs.getString("id"), rs.getString("title"), rs.getString("content")),
                literal, limit);
    }
}