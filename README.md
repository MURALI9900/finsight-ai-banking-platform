# FinSight AI — Banking Operations Intelligence Platform

AI-powered banking operations platform for bank officers.

## Current milestone
Demo banking data and backend APIs are available. No real bank API, company code, or production data is used.

### Services
- Banking Operations Service: port 8081
- Customer Service: port 8082

### APIs
Banking:
- GET /api/v1/banking/summary
- GET /api/v1/banking/loans
- GET /api/v1/banking/loans/{loanId}
- GET /api/v1/banking/loans/overdue?minimumDays=30
- GET /api/v1/banking/transactions
- GET /api/v1/banking/transactions/{transactionId}
- GET /api/v1/banking/transactions/failed
- GET /api/v1/system/health

Customer:
- GET /api/v1/customers
- GET /api/v1/customers/{id}
- GET /api/v1/customers/search?name=arun

## Run
cd backend/banking-operations-service && mvn spring-boot:run
cd backend/customer-service && mvn spring-boot:run

## AI design
The future AI assistant will call approved APIs/tools with authorization and audit logging. It will not receive unrestricted database or SQL access.

## Roadmap
1. Banking + customer APIs
2. API Gateway
3. JWT officer authentication
4. PostgreSQL persistence
5. AI service and tool calling
6. RAG and vector search
7. Kafka events
8. Audit service
9. Angular officer portal
10. Docker + CI/CD + deployment
