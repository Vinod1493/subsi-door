package in.gov.telangana.agri.farmeridentityservice.repository;

import in.gov.telangana.agri.farmeridentityservice.entity.LeaseRelationships;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LeaseRelationshipRepository extends JpaRepository<LeaseRelationships, UUID> {

    List<LeaseRelationships> findByOwnerFarmerId(UUID ownerFarmerId);

    List<LeaseRelationships> findByTenantFarmerId(UUID tenantFarmerId);

    @Query("""
        SELECT lr FROM LeaseRelationships lr
        WHERE lr.landProfile.id = :landProfileId
            """)
    Optional<LeaseRelationships> findActiveLeaseByLandProfileId(@Param("landProfileId") UUID landProfileId);
}
