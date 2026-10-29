#!/bin/bash
set -euo pipefail
BASE=${1:-http://localhost:8082}
PAYLOAD='{"name":"Ким Вячеслав Александрович","email":"kim-smoke-'"$(date +%s)-$$"'@example.com","age":21}'
request(){
 local expected=$1 method=$2 path=$3 data=${4:-}
 local body status
 body=$(mktemp)
 if [ -n "$data" ]; then
  status=$(curl -sS -o "$body" -w '%{http_code}' -X "$method" -H 'Content-Type: application/json' -d "$data" "$BASE$path")
 else
  status=$(curl -sS -o "$body" -w '%{http_code}' -X "$method" "$BASE$path")
 fi
 printf '%s %s -> %s\n' "$method" "$path" "$status" >&2
 cat "$body" >&2; printf '\n' >&2
 if [ "$status" != "$expected" ]; then rm -f "$body"; return 1; fi
 cat "$body"; rm -f "$body"
}
for i in $(seq 1 60); do
 if curl -fsS "$BASE/actuator/health" >/dev/null; then break; fi
 sleep 2
done
created=$(request 201 POST /api/students "$PAYLOAD")
id=$(printf '%s' "$created" | python3 -c 'import json,sys; print(json.load(sys.stdin)["id"])')
request 200 GET /api/students >/dev/null
request 200 GET "/api/students/$id" >/dev/null
request 200 PUT "/api/students/$id" '{"name":"Ким Вячеслав Александрович Updated","email":"kim-updated-'"$(date +%s)-$$"'@example.com","age":22}' >/dev/null
request 204 DELETE "/api/students/$id" >/dev/null
request 404 GET "/api/students/$id" >/dev/null
request 400 POST /api/students '{"name":"","email":"bad","age":0}' >/dev/null
echo 'PASS: CRUD, missing student, validation'
