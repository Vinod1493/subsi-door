# ─── Stage 1: Builder ─────────────────────────────────────────────────────────
# We use a full JDK image so Gradle can compile the project.
# The build context is the ROOT of the repo (set in docker-compose), so every
# module and the Gradle wrapper are visible here.
FROM eclipse-temurin:25-jdk AS builder

WORKDIR /workspace

# Copy the Gradle wrapper first — this layer is cached as long as the wrapper
# doesn't change, so Docker won't re-download Gradle on every build.
COPY gradlew .
COPY gradle gradle

# Copy all build definition files. Changes here invalidate the dependency cache.
COPY settings.gradle.kts .
COPY build.gradle.kts .

# Copy every module's build file so Gradle can configure the full project graph.
# Source code is NOT copied yet — this keeps the dependency-download layer separate.
COPY infra/eureka-server/build.gradle.kts  infra/eureka-server/build.gradle.kts
COPY infra/config-server/build.gradle.kts  infra/config-server/build.gradle.kts
COPY infra/api-gateway/build.gradle.kts    infra/api-gateway/build.gradle.kts

# Resolve and cache all dependencies without compiling anything.
# If only source files change later, this layer is reused from cache.
RUN ./gradlew :infra:eureka-server:dependencies --no-daemon -q

# Now copy the actual source code for this service and build the fat JAR.
COPY infra/eureka-server/src infra/eureka-server/src
RUN ./gradlew :infra:eureka-server:bootJar -x test --no-daemon


# ─── Stage 2: Runtime ─────────────────────────────────────────────────────────
# Slim JRE image — no compiler, no Gradle, just enough to run the JAR.
FROM eclipse-temurin:25-jre-alpine

# curl is needed for Docker health checks.
RUN apk add --no-cache curl

WORKDIR /app

# Pull only the built JAR from the builder stage — everything else is discarded.
COPY --from=builder /workspace/infra/eureka-server/build/libs/*.jar app.jar

EXPOSE 8761

ENTRYPOINT ["java", "-jar", "app.jar"]
