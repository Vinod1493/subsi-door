package in.gov.telangana.agri.farmeridentityservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "farmers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder                 
public class Farmer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "aadhaar_number", unique = true, nullable = false, length = 12)
    private String aadhaarNumber;
    
    @Column(name = "full_name", nullable = false)
    private String fullName;
    
    @Column(name = "mobile_number", nullable = false, length = 10)
    private String mobileNumber;
    
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    
    @Column(name = "gender", length = 10)
    private String gender;
    
    @Column(name = "district", nullable = false, length = 100)
    private String district;
    
    @Column(name = "mandal")
    private String mandal;
    
    @Column(name = "village")
    private String village;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "kyc_status", nullable = false, length = 20)
    private KYCStatus kycStatus = KYCStatus.PENDING;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "farmer", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @Builder.Default
    private List<LandProfile> landProfiles = new ArrayList<>();

    @OneToMany(mappedBy = "farmer", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @Builder.Default
    private List<AadhaarVerification> aadharVerifications = new ArrayList<>();

    public enum KYCStatus {
        PENDING,
        OTP_SENT,
        VERIFIED,
        REJECTED
    }
    
}
