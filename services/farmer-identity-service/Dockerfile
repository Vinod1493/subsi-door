FROM eclipse-temurin:25-jdk AS builder

WORKDIR /workspace

COPY gradlew .
COPY gradle gradle

COPY settings.gradle.kts .
COPY build.gradle.kts .

COPY infra/eureka-server/build.gradle.kts  infra/eureka-server/build.gradle.kts
COPY infra/config-server/build.gradle.kts  infra/config-server/build.gradle.kts
COPY infra/api-gateway/build.gradle.kts    infra/api-gateway/build.gradle.kts
COPY services/farmer-identity-service/build.gradle.kts services/farmer-identity-service/build.gradle.kts

RUN ./gradlew :services:farmer-identity-service:dependencies --no-daemon -q

COPY services/farmer-identity-service/src services/farmer-identity-service/src
RUN ./gradlew :services:farmer-identity-service:bootJar -x test --no-daemon


FROM eclipse-temurin:25-jre-alpine

RUN apk add --no-cache curl

WORKDIR /app

COPY --from=builder /workspace/services/farmer-identity-service/build/libs/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
