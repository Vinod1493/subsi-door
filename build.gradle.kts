plugins {
    alias(libs.plugins.spring.boot) apply false
    alias(libs.plugins.spring.dependency.management) apply false
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "io.spring.dependency-management")

    group = "in.gov.telangana.agri"
    version = "1.0.0"

    val catalog = rootProject.extensions.getByType<VersionCatalogsExtension>().named("libs")
    val springBootVersion = catalog.findVersion("spring-boot").get().requiredVersion
    val springCloudVersion = catalog.findVersion("spring-cloud").get().requiredVersion

    the<io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension>().apply {
        imports {
            mavenBom("org.springframework.boot:spring-boot-dependencies:$springBootVersion")
            mavenBom("org.springframework.cloud:spring-cloud-dependencies:$springCloudVersion")
        }
    }

    configure<JavaPluginExtension> {
        toolchain {
            languageVersion = JavaLanguageVersion.of(25)
        }
    }

    dependencies {
        "compileOnly"(catalog.findLibrary("lombok").get())
        "annotationProcessor"(catalog.findLibrary("lombok").get())
        "testImplementation"(catalog.findLibrary("spring-boot-test").get())
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    tasks.withType<JavaCompile> {
        options.compilerArgs.add("-parameters")
    }
}
