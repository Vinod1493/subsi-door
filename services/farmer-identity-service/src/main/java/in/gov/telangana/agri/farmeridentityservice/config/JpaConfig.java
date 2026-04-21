package in.gov.telangana.agri.farmeridentityservice.config;

import org.flywaydb.core.Flyway;
import org.springframework.boot.jpa.autoconfigure.EntityManagerFactoryDependsOnPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JpaConfig {

    // Forces the JPA EntityManagerFactory to wait for Flyway to run migrations
    // before it validates the schema. Without this, Hibernate validates against
    // an empty database and throws "missing table" errors.
    @Bean
    public static EntityManagerFactoryDependsOnPostProcessor entityManagerFactoryDependsOnPostProcessor() {
        return new EntityManagerFactoryDependsOnPostProcessor(Flyway.class);
    }
}
