package in.gov.telangana.agri.farmeridentityservice.dto.response;

import in.gov.telangana.agri.farmeridentityservice.entity.LeaseRelationships;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class LeaseResponse {
    private UUID id;
    private UUID ownerFarmerId;
    private String ownerFarmerName;
    private UUID tenantFarmerId;
    private String tenantFarmerName;
    private UUID landProfileId;
    private String surveyNumber;
    private LocalDate startDate;
    private LeaseRelationships.LeaseStatus status;
    private LocalDateTime createdAt;

    public static LeaseResponse from(LeaseRelationships lease) {
        return LeaseResponse.builder()
            .id(lease.getId())
            .ownerFarmerId(lease.getOwnerFarmer().getId())
            .ownerFarmerName(lease.getOwnerFarmer().getFullName())
            .tenantFarmerId(lease.getTenantFarmer().getId())
            .tenantFarmerName(lease.getTenantFarmer().getFullName())
            .landProfileId(lease.getLandProfile().getId())
            .surveyNumber(lease.getLandProfile().getSurveyNumber())
            .startDate(lease.getStartDate())
            .status(lease.getStatus())
            .createdAt(lease.getCreatedAt())
            .build();
    }
}
