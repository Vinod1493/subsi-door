package in.gov.telangana.agri.farmeridentityservice.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class EnrollmentRequest {
    
    @NotBlank(message = "Aadhaar number is required")
    @Pattern(regexp = "^[0-9]{12}$", message = "Aadhaar number must be 12 digits")
    private String aadhaarNumber;

    @NotBlank(message = "Full name is required")
    @Size(min=2,max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullName;

    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "^[6-9][0-9]{9}$", message = "Mobile number must be 10 digits and start with 6, 7, 8, or 9")
    private String mobileNumber;

    @NotBlank(message = "District is required")
    private String district;

    private String mandal;

    private String village;

    private String gender;

    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;
}
