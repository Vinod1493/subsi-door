plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
}

dependencies {
    implementation(libs.spring.cloud.eureka.server)
    implementation(libs.spring.boot.actuator)
}

// eureka-client-jersey3:2.0.5 is compiled against Jersey 3.x but Spring Boot 4.x BOM upgrades
// Jersey to 4.x, breaking Jersey3TransportClientFactories bean initialization at startup.
configurations.all {
    resolutionStrategy.eachDependency {
        if (requested.group.startsWith("org.glassfish.jersey")) {
            useVersion("3.0.5")
        }
    }
}
