# ─── Stage 1: Builder ─────────────────────────────────────────────────────────
FROM eclipse-temurin:25-jdk AS builder

WORKDIR /workspace

COPY gradlew .
COPY gradle gradle
COPY settings.gradle.kts .
COPY build.gradle.kts .

COPY infra/eureka-server/build.gradle.kts  infra/eureka-server/build.gradle.kts
COPY infra/config-server/build.gradle.kts  infra/config-server/build.gradle.kts
COPY infra/api-gateway/build.gradle.kts    infra/api-gateway/build.gradle.kts

RUN ./gradlew :infra:api-gateway:dependencies --no-daemon -q

COPY infra/api-gateway/src infra/api-gateway/src
RUN ./gradlew :infra:api-gateway:bootJar -x test --no-daemon


# ─── Stage 2: Runtime ─────────────────────────────────────────────────────────
FROM eclipse-temurin:25-jre-alpine

RUN apk add --no-cache curl

WORKDIR /app

COPY --from=builder /workspace/infra/api-gateway/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
