package in.gov.telangana.agri.farmeridentityservice.repository;

import in.gov.telangana.agri.farmeridentityservice.entity.AadhaarVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AadhaarVerificationRepository extends JpaRepository<AadhaarVerification, UUID> {
    
    Optional<AadhaarVerification> findTopByFarmerIdOrderByCreatedAtDesc(UUID farmerId);

    Optional<AadhaarVerification> findByTransactionId(String transactionId);

}
