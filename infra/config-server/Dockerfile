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

RUN ./gradlew :infra:config-server:dependencies --no-daemon -q

COPY infra/config-server/src infra/config-server/src
RUN ./gradlew :infra:config-server:bootJar -x test --no-daemon


# ─── Stage 2: Runtime ─────────────────────────────────────────────────────────
FROM eclipse-temurin:25-jre-alpine

RUN apk add --no-cache curl

WORKDIR /app

COPY --from=builder /workspace/infra/config-server/build/libs/*.jar app.jar

# Copy the config directory that holds per-service yml files.
# This is what the config server serves to other services at startup.
# In docker-compose we also mount this as a volume so you can edit
# configs without rebuilding the image.
COPY infra/config-server/config ./config

EXPOSE 8888

ENTRYPOINT ["java", "-jar", "app.jar"]
