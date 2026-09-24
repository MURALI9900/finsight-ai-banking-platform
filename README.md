# FinSight AI — Banking Operations Intelligence Platform

AI-powered banking operations platform for bank officers.

## Current milestone
The platform now combines PostgreSQL-backed banking operations, JWT officer authentication, approved banking tools, an LLM adapter, and retrieval-augmented policy context. The AI layer never receives unrestricted SQL access.

### AI flow
Officer question -> JWT Gateway -> AI Service -> approved banking tool -> RAG policy retrieval -> LLM -> explainable response

### RAG demo policies
- POL-001: Loan Repayment Policy
- POL-002: Overdue Loan Operations
- POL-003: Failed Transaction Handling

The current retriever is intentionally lightweight and deterministic for local demonstration. It can be replaced with an embedding/vector-store implementation without changing the banking tool boundary.

### Services
- API Gateway: 8080
- Banking Operations Service: 8081
- Customer Service: 8082
- Auth Service: 8083
- AI Service: 8084

### AI API
POST /api/v1/ai/ask — JWT required

Example:
```json
{"question":"Why did a loan repayment fail?"}
```

The response includes the approved tool used, banking data, and retrieved policy source IDs.

### Demo officer
Username: ops.officer
Password: Demo@123

Demo credentials only. Production secrets must use environment variables or a secret manager.

### Run
```bash
cd infrastructure/docker
docker compose up -d
```

Then start the five Spring Boot services from their respective directories.

### Roadmap
1. Banking + customer APIs
2. JWT + API Gateway
3. PostgreSQL
4. Approved AI tool calling
5. LLM integration
6. RAG + vector search
7. Kafka events
8. Audit service
9. Angular officer portal
10. Docker + CI/CD + deployment
