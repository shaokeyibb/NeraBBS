FROM gradle:8-jdk17 AS build
LABEL org.opencontainers.image.authors="HikariLan"

WORKDIR /build
COPY . .

RUN gradle build --no-daemon --parallel

FROM node:18 AS frontend
LABEL org.opencontainers.image.authors="HikariLan"

EXPOSE 3000

WORKDIR /app

COPY --from=build /build/frontend/.output .

ENTRYPOINT ["node" ,"server/index.mjs"]

FROM openjdk:17 AS service-auth
LABEL org.opencontainers.image.authors="HikariLan"

ARG SERVICE_NAME=service-auth

EXPOSE 8080

WORKDIR /app

COPY --from=build /build/$SERVICE_NAME/build/libs/$SERVICE_NAME-*.jar application.jar

ENTRYPOINT ["java", "-jar", "application.jar"]

FROM openjdk:17 AS service-user
LABEL org.opencontainers.image.authors="HikariLan"

ARG SERVICE_NAME=service-user

EXPOSE 8080

WORKDIR /app

COPY --from=build /build/$SERVICE_NAME/build/libs/$SERVICE_NAME-*.jar application.jar

ENTRYPOINT ["java", "-jar", "application.jar"]

FROM openjdk:17 AS service-post
LABEL org.opencontainers.image.authors="HikariLan"

ARG SERVICE_NAME=service-post

EXPOSE 8080

WORKDIR /app

COPY --from=build /build/$SERVICE_NAME/build/libs/$SERVICE_NAME-*.jar application.jar

ENTRYPOINT ["java", "-jar", "application.jar"]

FROM openjdk:17 AS gateway
LABEL org.opencontainers.image.authors="HikariLan"

ARG SERVICE_NAME=gateway

EXPOSE 80

WORKDIR /app

COPY --from=build /build/$SERVICE_NAME/build/libs/$SERVICE_NAME-*.jar application.jar

ENTRYPOINT ["java", "-jar", "application.jar"]