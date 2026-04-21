package in.gov.telangana.agri.farmeridentityservice.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OtpResponse {
    
    private boolean success;
    private String message;
    private String transactionId;
    private String maskedMobileNumber;
}
