# FinSight AI — Banking Operations Intelligence Platform

AI-powered banking operations platform for bank officers.

## Current milestone
Demo banking data, backend APIs, JWT officer authentication, and an API Gateway are available. No real bank API, company code, or production data is used.

### Services
- API Gateway: port 8080
- Banking Operations Service: port 8081
- Customer Service: port 8082
- Auth Service: port 8083

### Gateway APIs
- POST /api/v1/auth/login — public login endpoint
- GET /api/v1/banking/summary — JWT required
- GET /api/v1/banking/loans — JWT required
- GET /api/v1/banking/loans/{loanId} — JWT required
- GET /api/v1/banking/loans/overdue?minimumDays=30 — JWT required
- GET /api/v1/banking/transactions — JWT required
- GET /api/v1/banking/transactions/{transactionId} — JWT required
- GET /api/v1/banking/transactions/failed — JWT required
- GET /api/v1/customers — JWT required
- GET /api/v1/customers/{id} — JWT required
- GET /api/v1/customers/search?name=arun — JWT required

### Demo officer
- Username: ops.officer
- Password: Demo@123

This credential is for local/demo use only. Production credentials and JWT secrets must be externalized to a secret manager or environment variables.

## Run
Start the services in separate terminals:

cd backend/auth-service && mvn spring-boot:run
cd backend/banking-operations-service && mvn spring-boot:run
cd backend/customer-service && mvn spring-boot:run
cd backend/api-gateway && mvn spring-boot:run

## Authentication flow
1. Officer sends credentials to POST /api/v1/auth/login.
2. Auth Service issues a signed JWT containing the BANK_OFFICER role.
3. API Gateway validates the JWT before forwarding protected requests.
4. Gateway adds the authenticated officer and role as internal request headers.
5. Backend services receive only authorized requests.

The AI assistant will follow the same security boundary: it will call approved backend tools/APIs rather than receiving unrestricted database or SQL access.

## AI roadmap
1. Banking + customer APIs
2. API Gateway + JWT officer authentication
3. PostgreSQL persistence
4. AI service and tool calling
5. RAG and vector search
6. Kafka events
7. Audit service
8. Angular officer portal
9. Docker + CI/CD + deployment
