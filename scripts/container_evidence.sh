#!/bin/bash
set -euo pipefail
cd "$(dirname "$0")/.."
mkdir -p evidence
exec > evidence/containers.txt 2>&1
docker compose up -d --build
./scripts/api_smoke.sh
# Save only non-secret inspect fields, never the environment containing DB_PASSWORD.
docker compose ps
docker image inspect kim-vyacheslav-student-api:v1.0-kim-vyacheslav --format 'ID={{.Id}} Size={{.Size}} Created={{.Created}}'
APP=$(docker compose ps -q app)
docker inspect "$APP" --format 'Name={{.Name}} Image={{.Config.Image}} User={{.Config.User}} Networks={{json .NetworkSettings.Networks}}'
docker compose logs --tail=60 app
docker compose exec -T app java -version
created=$(curl -fsS -H 'Content-Type: application/json' -d '{"name":"Ким Вячеслав Александрович Persistence","email":"persist-'"$(date +%s)-$$"'@example.com","age":21}' http://localhost:${APP_PORT:-8082}/api/students)
id=$(printf '%s' "$created" | python3 -c 'import json,sys; print(json.load(sys.stdin)["id"])')
printf 'Before restart: %s\n' "$created"
docker compose stop app
docker compose start app
docker compose down # keep named volume, no -v
docker compose up -d
for i in $(seq 1 60); do
 if curl -fsS "http://localhost:${APP_PORT:-8082}/api/students/$id" > evidence/persistence-after.json; then break; fi
 sleep 2
done
python3 - "$created" <<'CHECK'
import json,sys
before=json.loads(sys.argv[1]); after=json.load(open('evidence/persistence-after.json'))
assert before==after, (before,after)
print('PASS: identical PostgreSQL record after compose down/up')
CHECK
# Rebuild with changed JAR metadata, preserving the graded original image.
docker build --build-arg APP_VERSION=1.0.1 -t kim-vyacheslav-student-api:v1.0.1-kim-vyacheslav .
docker image inspect kim-vyacheslav-student-api:v1.0.1-kim-vyacheslav --format 'ID={{.Id}} Size={{.Size}} Created={{.Created}}'
