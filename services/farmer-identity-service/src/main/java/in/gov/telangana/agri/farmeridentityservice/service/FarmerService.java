package in.gov.telangana.agri.farmeridentityservice.service;

import in.gov.telangana.agri.farmeridentityservice.config.JwtService;
import in.gov.telangana.agri.farmeridentityservice.dto.request.*;
import in.gov.telangana.agri.farmeridentityservice.dto.response.*;
import in.gov.telangana.agri.farmeridentityservice.entity.*;
import in.gov.telangana.agri.farmeridentityservice.exception.*;
import in.gov.telangana.agri.farmeridentityservice.mapper.FarmerMapper;
import in.gov.telangana.agri.farmeridentityservice.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FarmerService {

    private final FarmerRepository farmerRepository;
    private final LandProfileRepository landProfileRepository;
    private final AadhaarVerificationRepository aadhaarVerificationRepository;
    private final LeaseRelationshipRepository leaseRelationshipRepository;
    private final AadhaarService aadhaarService;
    private final JwtService jwtService;
    private final FarmerMapper farmerMapper;

    // ─────────────────────────────────────
    // ENROLLMENT
    // ─────────────────────────────────────

    @Transactional
    public FarmerResponse enroll(EnrollmentRequest request) {
        log.info("Enrolling farmer with Aadhaar: {}XXXXXXXX",
            request.getAadhaarNumber().substring(0, 4));

        if (farmerRepository.existsByAadhaarNumber(request.getAadhaarNumber())) {
            throw new DuplicateEnrollmentException(request.getAadhaarNumber());
        }

        Farmer farmer = farmerMapper.toEntity(request);
        farmer.setKycStatus(Farmer.KYCStatus.PENDING);
        Farmer saved = farmerRepository.save(farmer);

        log.info("Farmer enrolled successfully. ID: {}", saved.getId());
        return farmerMapper.toResponse(saved);
    }

    // ─────────────────────────────────────
    // SEND OTP
    // ─────────────────────────────────────

    @Transactional
    public OtpResponse sendOtp(SendOtpRequest request) {
        Farmer farmer = farmerRepository
            .findByAadhaarNumber(request.getAadhaarNumber())
            .orElseThrow(() -> new FarmerNotFoundException(
                "Farmer not enrolled with this Aadhaar number"));

        if (farmer.getKycStatus() == Farmer.KYCStatus.VERIFIED) {
            throw new AadhaarVerificationException("Farmer is already KYC verified");
        }

        AadhaarService.AadhaarOtpResult result =
            aadhaarService.sendOtp(request.getAadhaarNumber());

        if (!result.success()) {
            throw new AadhaarVerificationException(
                "Failed to send OTP: " + result.errorMessage());
        }

        AadhaarVerification verification = AadhaarVerification.builder()
            .farmer(farmer)
            .aadhaarNumber(request.getAadhaarNumber())
            .transactionId(result.transactionId())
            .otpSentAt(LocalDateTime.now())
            .status(AadhaarVerification.VerificationStatus.OTP_SENT)
            .build();
        aadhaarVerificationRepository.save(verification);

        farmer.setKycStatus(Farmer.KYCStatus.OTP_SENT);
        farmerRepository.save(farmer);

        return OtpResponse.builder()
            .success(true)
            .transactionId(result.transactionId())
            .maskedMobileNumber(result.maskedMobile())
            .message("OTP sent successfully to Aadhaar-linked mobile")
            .build();
    }

    // ─────────────────────────────────────
    // VERIFY OTP → ISSUE JWT
    // ─────────────────────────────────────

    @Transactional
    public AuthResponse verifyOtp(VerifyOtpRequest request) {
        Farmer farmer = farmerRepository
            .findByAadhaarNumber(request.getAadhaarNumber())
            .orElseThrow(() -> new FarmerNotFoundException(
                "Farmer not found with this Aadhaar number"));

        AadhaarVerification verification =
            aadhaarVerificationRepository
                .findTopByFarmerIdOrderByCreatedAtDesc(farmer.getId())
                .orElseThrow(() -> new AadhaarVerificationException(
                    "No OTP request found. Please request OTP first."));

        if (!verification.getTransactionId().equals(request.getTransactionId())) {
            throw new AadhaarVerificationException("Invalid transaction ID");
        }

        if (verification.getOtpSentAt()
                        .isBefore(LocalDateTime.now().minusMinutes(10))) {
            verification.setStatus(AadhaarVerification.VerificationStatus.EXPIRED);
            aadhaarVerificationRepository.save(verification);
            throw new AadhaarVerificationException(
                "OTP has expired. Please request a new OTP.");
        }

        boolean valid = aadhaarService.verifyOtp(
            request.getAadhaarNumber(),
            request.getOtp(),
            request.getTransactionId()
        );

        if (!valid) {
            verification.setStatus(AadhaarVerification.VerificationStatus.FAILED);
            verification.setFailureReason("Invalid OTP entered");
            aadhaarVerificationRepository.save(verification);
            throw new AadhaarVerificationException("Invalid OTP. Please try again.");
        }

        verification.setStatus(AadhaarVerification.VerificationStatus.VERIFIED);
        verification.setVerifiedAt(LocalDateTime.now());
        aadhaarVerificationRepository.save(verification);

        farmer.setKycStatus(Farmer.KYCStatus.VERIFIED);
        farmerRepository.save(farmer);

        String token = jwtService.generateToken(farmer.getId(), "FARMER");

        log.info("Farmer KYC verified and JWT issued. ID: {}", farmer.getId());

        return AuthResponse.builder()
            .accessToken(token)
            .tokenType("Bearer")
            .expiresIn(86400)
            .farmerId(farmer.getId())
            .fullName(farmer.getFullName())
            .kycStatus(farmer.getKycStatus().name())
            .build();
    }

    // ─────────────────────────────────────
    // GET FARMER PROFILE
    // ─────────────────────────────────────

    @Transactional(readOnly = true)
    public FarmerResponse getFarmerById(UUID farmerId) {
        Farmer farmer = farmerRepository.findById(farmerId)
            .orElseThrow(() -> new FarmerNotFoundException(farmerId));
        return farmerMapper.toResponse(farmer);
    }

    // ─────────────────────────────────────
    // ADD LAND PROFILE
    // ─────────────────────────────────────

    @Transactional
    public FarmerResponse addLandProfile(UUID farmerId, LandProfileRequest request) {
        Farmer farmer = farmerRepository.findById(farmerId)
            .orElseThrow(() -> new FarmerNotFoundException(farmerId));

        if (farmer.getKycStatus() != Farmer.KYCStatus.VERIFIED) {
            throw new AadhaarVerificationException(
                "KYC must be completed before adding land profiles");
        }

        if (landProfileRepository.existsByFarmerIdAndSurveyNumber(
                farmerId, request.getSurveyNumber())) {
            throw new IllegalArgumentException(
                "Land profile with survey number "
                + request.getSurveyNumber()
                + " already exists for this farmer");
        }

        LandProfile landProfile = LandProfile.builder()
            .farmer(farmer)
            .surveyNumber(request.getSurveyNumber())
            .areaAcres(java.math.BigDecimal.valueOf(request.getAreaAcres()))
            .soilType(request.getSoilType())
            .irrigationType(request.getIrrigationType())
            .previousCrop(request.getPreviousCrop())
            .isActive(true)
            .build();

        landProfileRepository.save(landProfile);

        return farmerMapper.toResponse(farmerRepository.findById(farmerId).get());
    }

    // ─────────────────────────────────────
    // SUBMIT LEASE
    // ─────────────────────────────────────

    @Transactional
    public LeaseRelationships submitLease(UUID ownerFarmerId,
                                          UUID tenantFarmerId,
                                          UUID landProfileId) {
        Farmer owner = farmerRepository.findById(ownerFarmerId)
            .orElseThrow(() -> new FarmerNotFoundException(ownerFarmerId));

        Farmer tenant = farmerRepository.findById(tenantFarmerId)
            .orElseThrow(() -> new FarmerNotFoundException(tenantFarmerId));

        LandProfile land = landProfileRepository.findById(landProfileId)
            .orElseThrow(() -> new IllegalArgumentException("Land profile not found"));

        if (!land.getFarmer().getId().equals(ownerFarmerId)) {
            throw new IllegalArgumentException(
                "Land profile does not belong to this farmer");
        }

        leaseRelationshipRepository
            .findActiveLeaseByLandProfileId(landProfileId)
            .ifPresent(existing -> {
                throw new IllegalArgumentException(
                    "An active lease already exists for this land");
            });

        return leaseRelationshipRepository.save(
            LeaseRelationships.builder()
                .ownerFarmer(owner)
                .tenantFarmer(tenant)
                .landProfile(land)
                .startDate(java.time.LocalDate.now())
                .status(LeaseRelationships.LeaseStatus.PENDING)
                .build()
        );
    }
}
