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
import java.util.UUID;

@Entity
@Table(name = "lease_relationships")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaseRelationships {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_farmer_id", nullable = false)
    private Farmer ownerFarmer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_farmer_id", nullable = false)
    private Farmer tenantFarmer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "land_profile_id", nullable = false)
    private LandProfile landProfile;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private LeaseStatus status = LeaseStatus.ACTIVE;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public enum LeaseStatus {
        PENDING, ACTIVE, EXPIRED, REJECTED
    }
    
}