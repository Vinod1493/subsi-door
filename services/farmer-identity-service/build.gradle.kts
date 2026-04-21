plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
}

dependencies {
    // Web
    implementation(libs.spring.boot.web)
    implementation(libs.spring.boot.webflux)      // for WebClient → UIDAI calls

    // Data
    implementation(libs.spring.boot.data.jpa)
    implementation(libs.postgresql)
    implementation(libs.flyway.core)
    implementation(libs.flyway.postgres)

    // Security
    implementation(libs.spring.boot.security)
    implementation(libs.jjwt.api)
    runtimeOnly(libs.jjwt.impl)
    runtimeOnly(libs.jjwt.jackson)

    // Validation
    implementation(libs.spring.boot.validation)

    // Spring Cloud
    implementation(libs.spring.cloud.config.client)
    implementation(libs.spring.cloud.eureka.client)

    // Actuator
    implementation(libs.spring.boot.actuator)

    // Utilities
    implementation(libs.mapstruct)
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
    annotationProcessor(libs.mapstruct.processor)
    annotationProcessor(libs.lombok.mapstruct.binding)

    // Test
    testImplementation(libs.spring.boot.test)
    testImplementation(libs.testcontainers.junit)
    testImplementation(libs.testcontainers.postgres)
}