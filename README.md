# FinSight AI — Banking Operations Intelligence Platform

AI-powered banking operations platform for bank officers.

## Current milestone

The first backend foundation is implemented with safe demo records. No external banking API or company data is used.

### Banking Operations Service

Spring Boot / Java 17 REST service running on port 8081.

Endpoints:

- GET /api/v1/banking/summary
- GET /api/v1/banking/loans
- GET /api/v1/banking/loans/overdue?minimumDays=30
- GET /api/v1/banking/transactions
- GET /api/v1/banking/transactions/failed

### Run locally

cd backend/banking-operations-service
mvn spring-boot:run

Then open:

http://localhost:8081/api/v1/banking/summary

## Planned AI capabilities

1. Officer authentication and authorization
2. Natural-language banking assistant
3. Controlled tool/API calling
4. RAG over banking policy documents
5. Explainable answers with source references
6. Kafka event processing
7. Audit trail
8. Angular officer dashboard
9. Docker and CI/CD

## Important security principle

The AI agent will not receive unrestricted database or SQL access. It will call approved backend tools with authorization checks and audited requests.
