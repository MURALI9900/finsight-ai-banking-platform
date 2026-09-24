# FinSight AI — Banking Operations Intelligence Platform

AI-powered banking operations platform for bank officers.

## Overview

FinSight AI helps authorized bank officers query operational banking data using natural language. It is designed for internal banking operations teams, not retail customers.

The platform combines PostgreSQL-backed banking operations, JWT officer authentication, an API Gateway, approved backend AI tools, LLM integration, semantic RAG policy retrieval with pgvector, Kafka events, audit persistence, and an Angular officer portal.

**Security principle:** the AI layer never receives unrestricted SQL/database access. AI requests are routed through approved backend tools with authorization and auditability.

## Architecture

Officer Portal (Angular)
-> Nginx
-> API Gateway
-> JWT Authentication
-> Banking / Customer / AI / Audit Services
-> PostgreSQL + pgvector
-> Kafka

AI flow:

Officer question -> JWT Gateway -> AI Service -> approved banking tool -> pgvector RAG policy retrieval -> LLM -> explainable response

Event flow:

Banking Operations Service -> Kafka `banking.operations.events` -> Audit Service -> PostgreSQL

## Features

- Officer login with signed JWT
- Role validation for `BANK_OFFICER`
- Banking loan and transaction APIs
- Overdue loan detection
- Failed transaction analysis
- Banking summary
- AI assistant for operational questions
- Approved tool-based AI access instead of direct SQL
- LLM adapter with environment-based API key
- pgvector semantic policy retrieval
- Deterministic RAG fallback when embeddings are unavailable
- Kafka operational events
- Persistent audit events
- PostgreSQL persistence
- Angular officer dashboard
- Docker Compose deployment
- Production Compose configuration with internal backend services isolated
- GitHub Actions CI/CD
- End-to-end smoke-test script

## RAG demo policies

- POL-001: Loan Repayment Policy
- POL-002: Overdue Loan Operations
- POL-003: Failed Transaction Handling

## Services

| Service | Port |
|---|---:|
| Officer Portal | 4200 |
| API Gateway | 8080 |
| Banking Operations Service | 8081 |
| Customer Service | 8082 |
| Auth Service | 8083 |
| AI Service | 8084 |
| Audit Service | 8085 |
| PostgreSQL + pgvector | 5432 |
| Kafka | 9092 |

## Demo data

The repository uses synthetic banking data only.

Demo officer:

- Username: `ops.officer`
- Password: `Demo@123`

These credentials are for portfolio demonstration only.

## Local Docker setup

From the repository root:

```bash
cd infrastructure/docker
cp .env.example .env
```

Edit `.env` and set at minimum:

```text
FINSIGHT_JWT_SECRET=your-long-random-secret
POSTGRES_PASSWORD=your-strong-password
```

For real LLM responses, also configure the LLM provider credentials/model in `.env`.

Start the complete local stack:

```bash
docker compose up --build
```

Open the officer portal at:

```text
http://localhost:4200
```

The portal uses same-origin `/api/` requests and Nginx proxies them to the API Gateway.

## Production-style Docker setup

For a deployment where only the portal and gateway are externally exposed:

```bash
cd infrastructure/docker
docker compose -f docker-compose.prod.yml up --build
```

The production Compose file keeps PostgreSQL, Kafka, authentication, banking, customer, AI, and audit services on the internal Docker network.

Use a managed secret store or deployment secret mechanism instead of committing `.env` or real credentials.

## Smoke test

After the local stack is running:

```bash
bash scripts/smoke-test.sh
```

The smoke test verifies:

1. Officer login and JWT issuance
2. Protected banking API access
3. Overdue-loan AI tool routing
4. Failed-transaction AI tool routing
5. Banking-summary AI tool routing
6. Protected customer API access

## API examples

### Login

```http
POST /api/v1/auth/login
Content-Type: application/json

{"username":"ops.officer","password":"Demo@123"}
```

### AI assistant

```http
POST /api/v1/ai/ask
Authorization: Bearer <JWT>
Content-Type: application/json

{"question":"Show me loans overdue by more than 30 days"}
```

The response includes the AI intent, approved tool used, banking data, answer, and retrieved policy source IDs.

### Kafka demo event

```http
POST /api/v1/banking/events/demo
Authorization: Bearer <JWT>
Content-Type: application/json

{
  "eventType": "TRANSACTION_FAILED",
  "entityId": "TXN9002",
  "customerId": "C1002",
  "description": "Synthetic demo transaction failed"
}
```

## CI/CD

GitHub Actions validates:

- Java 17 backend tests
- Angular build
- Docker image builds

Workflow file:

```text
.github/workflows/ci.yml
```

## Security notes

- JWT signing secrets are environment-based.
- Database credentials are environment-based.
- LLM API keys are environment-based.
- Local `.env` files are ignored by Git.
- AI has no unrestricted database/SQL access.
- Production Compose does not publish internal service ports.
- Demo credentials are intentionally simple and must not be reused for production.
- Production deployments should use a managed identity/secret solution, HTTPS, stronger authentication, centralized authorization, and monitoring.

## Repository structure

```text
backend/
  api-gateway/
  auth-service/
  banking-operations-service/
  customer-service/
  ai-service/
  audit-service/

frontend/
  officer-portal/

infrastructure/
  docker/
    docker-compose.yml
    docker-compose.prod.yml
    .env.example

scripts/
  smoke-test.sh

.github/
  workflows/
    ci.yml
```

## Portfolio positioning

FinSight AI demonstrates practical enterprise BFSI engineering across:

- Java 17
- Spring Boot
- Microservices
- REST APIs
- JWT security
- PostgreSQL
- pgvector / RAG
- LLM integration
- Kafka
- Angular
- Docker
- CI/CD
- AI tool-based architecture
- Auditability and explainability

This project is a portfolio/demo system and does not contain real customer or bank data.
