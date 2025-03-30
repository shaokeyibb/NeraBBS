FROM node:22-slim AS build-frontend
LABEL org.opencontainers.image.authors="HikariLan"

ENV PNPM_HOME="/pnpm"
ENV PATH="$PNPM_HOME:$PATH"
RUN corepack enable

WORKDIR /build/frontend
COPY ./frontend/ .

RUN --mount=type=cache,id=build-frontend,target=/pnpm/store pnpm install --frozen-lockfile
RUN pnpm run build

FROM gradle:8-jdk21 AS build
LABEL org.opencontainers.image.authors="HikariLan"

ENV GRADLE_USER_HOME="/gradle"
ENV PATH="$GRADLE_USER_HOME:$PATH"

WORKDIR /build
COPY . .

RUN --mount=type=cache,id=build,target=/gradle/cache gradle build -x :frontend:build --no-daemon --parallel

FROM node:22-slim AS frontend
LABEL org.opencontainers.image.authors="HikariLan"

EXPOSE 3000

WORKDIR /app

COPY --from=build-frontend /build/frontend/dist .

RUN npm install --global serve

ENTRYPOINT ["serve"]

FROM openjdk:21 AS service-auth
LABEL org.opencontainers.image.authors="HikariLan"

ARG SERVICE_NAME=service-auth
ENV ENV_SERVICE_NAME=$SERVICE_NAME

EXPOSE 8080

WORKDIR /app

COPY --from=build /build/$SERVICE_NAME/build/libs/$SERVICE_NAME-*.jar application.jar
COPY --from=build /build/binary-tools/opentelemetry-javaagent.jar opentelemetry-javaagent.jar

ENTRYPOINT java -javaagent:opentelemetry-javaagent.jar -Dotel.service.name=$ENV_SERVICE_NAME -jar application.jar

FROM openjdk:21 AS service-user
LABEL org.opencontainers.image.authors="HikariLan"

ARG SERVICE_NAME=service-user
ENV ENV_SERVICE_NAME=$SERVICE_NAME

EXPOSE 8080

WORKDIR /app

COPY --from=build /build/$SERVICE_NAME/build/libs/$SERVICE_NAME-*.jar application.jar
COPY --from=build /build/binary-tools/opentelemetry-javaagent.jar opentelemetry-javaagent.jar

ENTRYPOINT java -javaagent:opentelemetry-javaagent.jar -Dotel.service.name=$ENV_SERVICE_NAME -jar application.jar

FROM openjdk:21 AS service-user-profile
LABEL org.opencontainers.image.authors="HikariLan"

ARG SERVICE_NAME=service-user-profile
ENV ENV_SERVICE_NAME=$SERVICE_NAME

EXPOSE 8080

WORKDIR /app

COPY --from=build /build/$SERVICE_NAME/build/libs/$SERVICE_NAME-*.jar application.jar
COPY --from=build /build/binary-tools/opentelemetry-javaagent.jar opentelemetry-javaagent.jar

ENTRYPOINT java -javaagent:opentelemetry-javaagent.jar -Dotel.service.name=$ENV_SERVICE_NAME -jar application.jar

FROM openjdk:21 AS service-post
LABEL org.opencontainers.image.authors="HikariLan"

ARG SERVICE_NAME=service-post
ENV ENV_SERVICE_NAME=$SERVICE_NAME

EXPOSE 8080

WORKDIR /app

COPY --from=build /build/$SERVICE_NAME/build/libs/$SERVICE_NAME-*.jar application.jar
COPY --from=build /build/binary-tools/opentelemetry-javaagent.jar opentelemetry-javaagent.jar

ENTRYPOINT java -javaagent:opentelemetry-javaagent.jar -Dotel.service.name=$ENV_SERVICE_NAME -jar application.jar

FROM openjdk:21 AS service-webauthn
LABEL org.opencontainers.image.authors="HikariLan"

ARG SERVICE_NAME=service-webauthn
ENV ENV_SERVICE_NAME=$SERVICE_NAME

EXPOSE 8080

WORKDIR /app

COPY --from=build /build/$SERVICE_NAME/build/libs/$SERVICE_NAME-*.jar application.jar
COPY --from=build /build/binary-tools/opentelemetry-javaagent.jar opentelemetry-javaagent.jar

ENTRYPOINT java -javaagent:opentelemetry-javaagent.jar -Dotel.service.name=$ENV_SERVICE_NAME -jar application.jar

FROM openjdk:21 AS service-oss
LABEL org.opencontainers.image.authors="HikariLan"

ARG SERVICE_NAME=service-oss
ENV ENV_SERVICE_NAME=$SERVICE_NAME

EXPOSE 8080

WORKDIR /app

COPY --from=build /build/$SERVICE_NAME/build/libs/$SERVICE_NAME-*.jar application.jar
COPY --from=build /build/binary-tools/opentelemetry-javaagent.jar opentelemetry-javaagent.jar

ENTRYPOINT java -javaagent:opentelemetry-javaagent.jar -Dotel.service.name=$ENV_SERVICE_NAME -jar application.jar

FROM openjdk:21 AS service-search
LABEL org.opencontainers.image.authors="HikariLan"

ARG SERVICE_NAME=service-search
ENV ENV_SERVICE_NAME=$SERVICE_NAME

EXPOSE 8080

WORKDIR /app

COPY --from=build /build/$SERVICE_NAME/build/libs/$SERVICE_NAME-*.jar application.jar
COPY --from=build /build/binary-tools/opentelemetry-javaagent.jar opentelemetry-javaagent.jar

ENTRYPOINT java -javaagent:opentelemetry-javaagent.jar -Dotel.service.name=$ENV_SERVICE_NAME -jar application.jar

FROM openjdk:21 AS service-comment
LABEL org.opencontainers.image.authors="HikariLan"

ARG SERVICE_NAME=service-comment
ENV ENV_SERVICE_NAME=$SERVICE_NAME

EXPOSE 8080

WORKDIR /app

COPY --from=build /build/$SERVICE_NAME/build/libs/$SERVICE_NAME-*.jar application.jar
COPY --from=build /build/binary-tools/opentelemetry-javaagent.jar opentelemetry-javaagent.jar

ENTRYPOINT java -javaagent:opentelemetry-javaagent.jar -Dotel.service.name=$ENV_SERVICE_NAME -jar application.jar

FROM openjdk:21 AS gateway
LABEL org.opencontainers.image.authors="HikariLan"

ARG SERVICE_NAME=gateway
ENV ENV_SERVICE_NAME=$SERVICE_NAME

EXPOSE 8080

WORKDIR /app

COPY --from=build /build/$SERVICE_NAME/build/libs/$SERVICE_NAME-*.jar application.jar
COPY --from=build /build/binary-tools/opentelemetry-javaagent.jar opentelemetry-javaagent.jar

ENTRYPOINT java -javaagent:opentelemetry-javaagent.jar -Dotel.service.name=$ENV_SERVICE_NAME -jar application.jar

FROM openjdk:21 AS middleware-config
LABEL org.opencontainers.image.authors="HikariLan"

ARG SERVICE_NAME=middleware-config
ENV ENV_SERVICE_NAME=$SERVICE_NAME

EXPOSE 8080

WORKDIR /app

COPY --from=build /build/$SERVICE_NAME/build/libs/$SERVICE_NAME-*.jar application.jar
COPY --from=build /build/binary-tools/opentelemetry-javaagent.jar opentelemetry-javaagent.jar

ENTRYPOINT java -javaagent:opentelemetry-javaagent.jar -Dotel.service.name=$ENV_SERVICE_NAME -jar application.jar