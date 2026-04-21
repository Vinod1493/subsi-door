package in.gov.telangana.agri.farmeridentityservice.dto.request;

import in.gov.telangana.agri.farmeridentityservice.entity.LandProfile;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class LandProfileRequest {
    
    @NotBlank(message = "Survey number is required")
    private String surveyNumber;
    
    @NotNull(message = "Area in acres is required")
    @DecimalMin(value = "0.01", message = "Area must be greater than 0")
    @DecimalMax(value = "10000.00", message = "Area must be less than or equal to 10000 acres")
    private Double areaAcres;

    private LandProfile.SoilType soilType;
    private LandProfile.IrrigationType irrigationType;
    private String previousCrop;
}
