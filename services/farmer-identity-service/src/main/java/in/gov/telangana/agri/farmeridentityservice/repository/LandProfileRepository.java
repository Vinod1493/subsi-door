package in.gov.telangana.agri.farmeridentityservice.repository;

import in.gov.telangana.agri.farmeridentityservice.entity.LandProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LandProfileRepository extends JpaRepository<LandProfile, UUID> {
    
    List<LandProfile> findByFarmerId(UUID farmerId);

    List<LandProfile> findByFarmerIdAndIsActiveTrue(UUID farmerId);

    boolean existsByFarmerIdAndSurveyNumber(UUID farmerId, String surveyNumber);

}
