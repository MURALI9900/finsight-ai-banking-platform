#!/usr/bin/env bash
set -euo pipefail

BASE_URL="${FINSIGHT_BASE_URL:-http://localhost:8080}"
USERNAME="${FINSIGHT_DEMO_USERNAME:-ops.officer}"
PASSWORD="${FINSIGHT_DEMO_PASSWORD:-Demo@123}"

echo "1/6 Checking gateway login..."
LOGIN_RESPONSE="$(curl -fsS -X POST "${BASE_URL}/api/v1/auth/login" \
  -H "Content-Type: application/json" \
  -d "{\"username\":\"${USERNAME}\",\"password\":\"${PASSWORD}\"}")"

TOKEN="$(printf '%s' "${LOGIN_RESPONSE}" | python -c 'import json,sys; print(json.load(sys.stdin)["token"])')"

if [ -z "${TOKEN}" ]; then
  echo "Login succeeded but no JWT token was returned."
  exit 1
fi

AUTH_HEADER="Authorization: Bearer ${TOKEN}"

echo "2/6 Checking banking API..."
curl -fsS "${BASE_URL}/api/v1/banking/loans" -H "${AUTH_HEADER}" >/dev/null

echo "3/6 Checking overdue-loan tool path..."
OVERDUE="$(curl -fsS "${BASE_URL}/api/v1/ai/ask" \
  -H "${AUTH_HEADER}" \
  -H "Content-Type: application/json" \
  -d '{"question":"Show me loans overdue by more than 30 days"}')"
printf '%s' "${OVERDUE}" | python -c 'import json,sys; d=json.load(sys.stdin); assert d.get("intent")=="OVERDUE_LOANS", d; print("   AI intent:", d.get("intent")); print("   Tool:", d.get("toolUsed"))'

echo "4/6 Checking failed-transaction tool path..."
FAILED="$(curl -fsS "${BASE_URL}/api/v1/ai/ask" \
  -H "${AUTH_HEADER}" \
  -H "Content-Type: application/json" \
  -d '{"question":"Show failed transactions"}')"
printf '%s' "${FAILED}" | python -c 'import json,sys; d=json.load(sys.stdin); assert d.get("intent")=="FAILED_TRANSACTIONS", d; print("   AI intent:", d.get("intent")); print("   Tool:", d.get("toolUsed"))'

echo "5/6 Checking banking summary path..."
SUMMARY="$(curl -fsS "${BASE_URL}/api/v1/ai/ask" \
  -H "${AUTH_HEADER}" \
  -H "Content-Type: application/json" \
  -d '{"question":"Give me the banking summary"}')"
printf '%s' "${SUMMARY}" | python -c 'import json,sys; d=json.load(sys.stdin); assert d.get("intent")=="BANKING_SUMMARY", d; print("   AI intent:", d.get("intent")); print("   Tool:", d.get("toolUsed"))'

echo "6/6 Checking protected customer API..."
curl -fsS "${BASE_URL}/api/v1/customers" -H "${AUTH_HEADER}" >/dev/null

echo
echo "FinSight smoke test PASSED."
