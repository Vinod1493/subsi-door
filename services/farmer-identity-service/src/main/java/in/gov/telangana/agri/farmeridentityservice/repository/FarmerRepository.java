package in.gov.telangana.agri.farmeridentityservice.repository;

import in.gov.telangana.agri.farmeridentityservice.entity.Farmer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FarmerRepository extends JpaRepository<Farmer, UUID> {

    Optional<Farmer> findByAadhaarNumber(String aadhaarNumber);

    List<Farmer> findByDistrict(String district);

    boolean existsByAadhaarNumber(String aadhaarNumber);

    @Query("""
        SELECT f FROM Farmer f
        LEFT JOIN FETCH f.landProfiles lp
        WHERE f.id = :farmerId
        AND lp.isActive = true
            """)
    Optional<Farmer> findByIdWithActiveLandProfiles(@Param("farmerId") UUID farmerId);
}
