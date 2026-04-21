package in.gov.telangana.agri.farmeridentityservice.repository;

import in.gov.telangana.agri.farmeridentityservice.entity.Farmer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
class FarmerRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("farmer_identity_test")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private FarmerRepository farmerRepository;

    @BeforeEach
    void cleanUp() {
        farmerRepository.deleteAll();
    }

    private Farmer buildFarmer(String aadhaarNumber, String district) {
        return Farmer.builder()
                .aadhaarNumber(aadhaarNumber)
                .fullName("Test Farmer")
                .mobileNumber("9876543210")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .gender("Male")
                .district(district)
                .mandal("Mandal A")
                .village("Village X")
                .kycStatus(Farmer.KYCStatus.PENDING)
                .build();
    }

    @Test
    void saveFarmer_persistsAndAssignsUUID() {
        Farmer saved = farmerRepository.save(buildFarmer("123456789012", "Hyderabad"));

        assertThat(saved.getId()).isNotNull();
        assertThat(farmerRepository.count()).isEqualTo(1);
    }

    @Test
    void findByAadhaarNumber_returnsCorrectFarmer() {
        farmerRepository.save(buildFarmer("111111111111", "Nizamabad"));

        Optional<Farmer> result = farmerRepository.findByAadhaarNumber("111111111111");

        assertThat(result).isPresent();
        assertThat(result.get().getFullName()).isEqualTo("Test Farmer");
    }

    @Test
    void findByAadhaarNumber_returnsEmpty_whenNotFound() {
        Optional<Farmer> result = farmerRepository.findByAadhaarNumber("000000000000");

        assertThat(result).isEmpty();
    }

    @Test
    void findByDistrict_returnsAllFarmersInDistrict() {
        farmerRepository.save(buildFarmer("111111111111", "Warangal"));
        farmerRepository.save(buildFarmer("222222222222", "Warangal"));
        farmerRepository.save(buildFarmer("333333333333", "Karimnagar"));

        List<Farmer> warangalFarmers = farmerRepository.findByDistrict("Warangal");

        assertThat(warangalFarmers).hasSize(2);
        assertThat(warangalFarmers).allMatch(f -> f.getDistrict().equals("Warangal"));
    }

    @Test
    void existsByAadhaarNumber_returnsTrue_whenExists() {
        farmerRepository.save(buildFarmer("999999999999", "Khammam"));

        assertThat(farmerRepository.existsByAadhaarNumber("999999999999")).isTrue();
    }

    @Test
    void existsByAadhaarNumber_returnsFalse_whenNotExists() {
        assertThat(farmerRepository.existsByAadhaarNumber("000000000000")).isFalse();
    }

    @Test
    void kycStatus_defaultsToPending() {
        Farmer saved = farmerRepository.save(buildFarmer("123456789012", "Hyderabad"));

        assertThat(saved.getKycStatus()).isEqualTo(Farmer.KYCStatus.PENDING);
    }
}
