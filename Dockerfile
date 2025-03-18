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

FROM node:22 AS frontend
LABEL org.opencontainers.image.authors="HikariLan"

EXPOSE 3000

WORKDIR /app

COPY --from=build-frontend /build/frontend/dist .

RUN npm install --global serve

ENTRYPOINT ["serve"]

FROM openjdk:21 AS service-auth
LABEL org.opencontainers.image.authors="HikariLan"

ARG SERVICE_NAME=service-auth

EXPOSE 8080

WORKDIR /app

COPY --from=build /build/$SERVICE_NAME/build/libs/$SERVICE_NAME-*.jar application.jar

ENTRYPOINT ["java", "-jar", "application.jar"]

FROM openjdk:21 AS service-user
LABEL org.opencontainers.image.authors="HikariLan"

ARG SERVICE_NAME=service-user

EXPOSE 8080

WORKDIR /app

COPY --from=build /build/$SERVICE_NAME/build/libs/$SERVICE_NAME-*.jar application.jar

ENTRYPOINT ["java", "-jar", "application.jar"]

FROM openjdk:21 AS service-user-profile
LABEL org.opencontainers.image.authors="HikariLan"

ARG SERVICE_NAME=service-user-profile

EXPOSE 8080

WORKDIR /app

COPY --from=build /build/$SERVICE_NAME/build/libs/$SERVICE_NAME-*.jar application.jar

ENTRYPOINT ["java", "-jar", "application.jar"]

FROM openjdk:21 AS service-post
LABEL org.opencontainers.image.authors="HikariLan"

ARG SERVICE_NAME=service-post

EXPOSE 8080

WORKDIR /app

COPY --from=build /build/$SERVICE_NAME/build/libs/$SERVICE_NAME-*.jar application.jar

ENTRYPOINT ["java", "-jar", "application.jar"]

FROM openjdk:21 AS service-webauthn
LABEL org.opencontainers.image.authors="HikariLan"

ARG SERVICE_NAME=service-webauthn

EXPOSE 8080

WORKDIR /app

COPY --from=build /build/$SERVICE_NAME/build/libs/$SERVICE_NAME-*.jar application.jar

ENTRYPOINT ["java", "-jar", "application.jar"]

FROM openjdk:21 AS service-oss
LABEL org.opencontainers.image.authors="HikariLan"

ARG SERVICE_NAME=service-oss

EXPOSE 8080

WORKDIR /app

COPY --from=build /build/$SERVICE_NAME/build/libs/$SERVICE_NAME-*.jar application.jar

ENTRYPOINT ["java", "-jar", "application.jar"]

FROM openjdk:21 AS service-search
LABEL org.opencontainers.image.authors="HikariLan"

ARG SERVICE_NAME=service-search

EXPOSE 8080

WORKDIR /app

COPY --from=build /build/$SERVICE_NAME/build/libs/$SERVICE_NAME-*.jar application.jar

ENTRYPOINT ["java", "-jar", "application.jar"]

FROM openjdk:21 AS gateway
LABEL org.opencontainers.image.authors="HikariLan"

ARG SERVICE_NAME=gateway

EXPOSE 80

WORKDIR /app

COPY --from=build /build/$SERVICE_NAME/build/libs/$SERVICE_NAME-*.jar application.jar

ENTRYPOINT ["java", "-jar", "application.jar"]