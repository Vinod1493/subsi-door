plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
}

rootProject.name = "subsi-door"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

// Uncomment each line only after generating that service via Spring Initializr

// Infrastructure
include("infra:eureka-server")
include("infra:config-server")
// include("infra:api-gateway")

// Shared libs
// include("shared-libs:common-dto")
// include("shared-libs:common-exceptions")
// include("shared-libs:common-security")

// Domain services
// include("services:farmer-identity-service")
// include("services:allocation-service")
// include("services:order-delivery-service")
// include("services:lease-approval-service")
// include("services:notification-service")

// AI services
// include("ai-services:crop-recommendation-service")
// include("ai-services:chatbot-service")