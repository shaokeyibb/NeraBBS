#!/usr/bin/env bash

BASE_DIR=$(cd "$(dirname "$0")" && pwd)

if [ "$1" == "stop" ]; then
  cd "$BASE_DIR" && docker compose down
  kill -9 "$(lsof -t -i:3000)" 2>/dev/null
  for port in $(seq 30000 30100); do
    kill -9 "$(lsof -t -i:"$port")" 2>/dev/null
  done
  exit 0
fi

cd "$BASE_DIR" && docker compose up -d || exit 1

export SPRING_DATASOURCE_URL="jdbc:postgresql://localhost:5432/nerabbs"
export SPRING_DATA_REDIS_HOST="localhost"
export SPRING_CLOUD_CONSUL_HOST="localhost"
export MINIO_ENDPOINT="http://localhost:9000"
export MEILISEARCH_ENDPOINT="http://localhost:7700"

export OTEL_EXPORTER_OTLP_ENDPOINT="http://localhost:4318"
export OTEL_EXPORTER_OTLP_PROTOCOL="http/protobuf"
export OTEL_LOGS_EXPORTER="otlp"
export OTEL_INSTRUMENTATION_HTTP_CLIENT_CAPTURE_REQUEST_HEADERS="X-User-ID"

export NERABBS_FRONTEND_URL="http://localhost:3000"

export POSTGRES_PASSWORD="nerabbs"
export POSTGRES_USER="nerabbs"
export POSTGRES_DB="nerabbs"

export MINIO_ROOT_USER="nerabbs"
export MINIO_ROOT_PASSWORD="nerabbs#strong_password@2023"

export MEILI_MASTER_KEY="nerabbs#strong_password@2023"

export NERABBS_WEBAUTHN_RELYINGPARTY_ID="localhost"
export NERABBS_WEBAUTHN_RELYINGPARTY_NAME="Nerabbs"

export SPRING_CLOUD_CONSUL_DISCOVERY_HOSTNAME="localhost"

PORT_BASE=0

cd "$BASE_DIR"/../ && ./gradlew build || exit 1

mkdir -p "$BASE_DIR"/"logs"

export SERVER_PORT=$((30000 + PORT_BASE))
export GRPC_SERVER_PORT=$((31000 + PORT_BASE))
PORT_BASE=$((PORT_BASE+1))
echo "Starting Middleware Config with port $SERVER_PORT"
java -javaagent:"$BASE_DIR"/../binary-tools/opentelemetry-javaagent.jar -Dotel.service.name=middleware-config -jar "$BASE_DIR"/../middleware-config/build/libs/middleware-config-1.0-SNAPSHOT.jar &> "$BASE_DIR"/logs/middleware-config.log &

# wait for config server start
sleep 10

services=(
  "gateway"
  "service-auth"
  "service-comment"
  "service-oss"
  "service-post"
  "service-search"
  "service-user"
  "service-user-profile"
  "service-webauthn"
)

for service in "${services[@]}"; do
  export SERVER_PORT=$((30000 + PORT_BASE))
  export GRPC_SERVER_PORT=$((31000 + PORT_BASE))
  PORT_BASE=$((PORT_BASE+1))
  echo "Starting $service with port $SERVER_PORT"
  java -javaagent:"$BASE_DIR"/../binary-tools/opentelemetry-javaagent.jar -Dotel.service.name="$service" -jar "$BASE_DIR"/../"$service"/build/libs/"$service"-1.0-SNAPSHOT.jar &> "$BASE_DIR"/logs/"$service".log &
done

cd "$BASE_DIR"/../frontend && pnpm dev --port 3000 &

echo "Dev Environment Start Successfully, now access http://localhost to your service"