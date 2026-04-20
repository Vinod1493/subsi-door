plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
}

dependencies {
    implementation(libs.spring.cloud.gateway)
    implementation(libs.spring.cloud.eureka.client)
    implementation(libs.spring.cloud.config.client)
    implementation(libs.spring.cloud.loadbalancer)
    implementation(libs.spring.boot.actuator)
    implementation(libs.jjwt.api)
    runtimeOnly(libs.jjwt.impl)
    runtimeOnly(libs.jjwt.jackson)
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
    testImplementation(libs.spring.boot.test)
}