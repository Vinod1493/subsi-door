package in.gov.telangana.agri.farmeridentityservice.dto.response;

import in.gov.telangana.agri.farmeridentityservice.entity.Farmer;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class FarmerResponse {
    
    private UUID id;
    private String fullName;
    private String mobileNumber;
    private String aadhaarLastFourDigits;
    private LocalDate dateOfBirth;
    private String gender;
    private String district;
    private String mandal;
    private String village;
    private Farmer.KYCStatus kycStatus;
    private LocalDateTime createdAt;

}
