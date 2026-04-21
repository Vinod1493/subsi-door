package in.gov.telangana.agri.farmeridentityservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "land_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LandProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farmer_id", nullable = false)
    private Farmer farmer;

    @Column(name = "survey_number", nullable = false, length = 50)
    private String surveyNumber;

    @Column(name = "area_acres", nullable = false, precision = 10, scale = 2)
    private BigDecimal areaAcres;

    @Enumerated(EnumType.STRING)
    @Column(name = "soil_type", length = 50)
    private SoilType soilType;

    @Enumerated(EnumType.STRING)
    @Column(name = "irrigation_type", length = 50)
    private IrrigationType irrigationType;

    @Column(name = "previous_crop", length = 100)
    private String previousCrop;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public enum SoilType {
        LOAMY, CLAYEY, SANDY, SILTY, PEATY, CHALKY
    }

    public enum IrrigationType {
        CANAL, WELLS, SPRINKLER, DROPLET
    }

}