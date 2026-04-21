package in.gov.telangana.agri.farmeridentityservice.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class AuthResponse {
    
    private String accessToken;
    private String tokenType = "Bearer";
    private long expiresIn;
    private UUID farmerId;
    private String fullName;
    private String kycStatus;

}
