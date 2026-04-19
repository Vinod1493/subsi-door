plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
}

dependencies {
    implementation(libs.spring.cloud.config.server)
    implementation(libs.spring.cloud.eureka.client)
    implementation(libs.spring.boot.actuator)
}