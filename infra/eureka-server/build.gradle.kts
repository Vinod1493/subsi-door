plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
}

// Spring Boot 4.x BOM upgrades Jersey to 4.x, but eureka-client-jersey3 requires Jersey 3.x.
// Overriding here via the BOM property keeps it scoped to this module only.
extra["jersey.version"] = "3.0.5"

dependencies {
    implementation(libs.spring.cloud.eureka.server)
    implementation(libs.spring.boot.actuator)
}
