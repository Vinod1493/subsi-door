package in.gov.telangana.agri.farmeridentityservice.controller;

import in.gov.telangana.agri.farmeridentityservice.dto.request.*;
import in.gov.telangana.agri.farmeridentityservice.dto.response.*;
import in.gov.telangana.agri.farmeridentityservice.dto.response.LeaseResponse;
import in.gov.telangana.agri.farmeridentityservice.entity.LeaseRelationships;
import in.gov.telangana.agri.farmeridentityservice.service.FarmerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/farmers")
@RequiredArgsConstructor
public class FarmerController {

    private final FarmerService farmerService;

    // ─────────────────────────────────────────────────────
    // POST /api/v1/farmers/enroll
    // Public endpoint — no JWT required
    // ─────────────────────────────────────────────────────
    @PostMapping("/enroll")
    public ResponseEntity<ApiResponse<FarmerResponse>> enroll(
            @Valid @RequestBody EnrollmentRequest request) {

        log.info("Enrollment request received");
        FarmerResponse response = farmerService.enroll(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(response));
    }

    // ─────────────────────────────────────────────────────
    // POST /api/v1/farmers/send-otp
    // Public endpoint — farmer requests OTP before login
    // ─────────────────────────────────────────────────────
    @PostMapping("/send-otp")
    public ResponseEntity<ApiResponse<OtpResponse>> sendOtp(
            @Valid @RequestBody SendOtpRequest request) {

        log.info("OTP request received");
        OtpResponse response = farmerService.sendOtp(request);
        return ResponseEntity.ok(
            ApiResponse.success(response));
    }

    // ─────────────────────────────────────────────────────
    // POST /api/v1/farmers/verify-otp
    // Public endpoint — verifies OTP and returns JWT
    // ─────────────────────────────────────────────────────
    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<AuthResponse>> verifyOtp(
            @Valid @RequestBody VerifyOtpRequest request) {

        log.info("OTP verification request received");
        AuthResponse response = farmerService.verifyOtp(request);
        return ResponseEntity.ok(
            ApiResponse.success(response));
    }

    // ─────────────────────────────────────────────────────
    // GET /api/v1/farmers/{id}
    // Protected — requires valid JWT
    // ─────────────────────────────────────────────────────
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FarmerResponse>> getFarmer(
            @PathVariable UUID id) {

        FarmerResponse response = farmerService.getFarmerById(id);
        return ResponseEntity.ok(
            ApiResponse.success(response));
    }

    // ─────────────────────────────────────────────────────
    // PUT /api/v1/farmers/{id}/land
    // Protected — farmer must be KYC verified
    // ─────────────────────────────────────────────────────
    @PutMapping("/{id}/land")
    public ResponseEntity<ApiResponse<FarmerResponse>> addLandProfile(
            @PathVariable UUID id,
            @Valid @RequestBody LandProfileRequest request) {

        FarmerResponse response =
            farmerService.addLandProfile(id, request);
        return ResponseEntity.ok(
            ApiResponse.success(response));
    }

    // ─────────────────────────────────────────────────────
    // POST /api/v1/farmers/{id}/lease
    // Protected — initiates a lease relationship
    // ─────────────────────────────────────────────────────
    @PostMapping("/{id}/lease")
    public ResponseEntity<ApiResponse<LeaseResponse>> submitLease(
            @PathVariable UUID id,
            @RequestParam UUID tenantFarmerId,
            @RequestParam UUID landProfileId) {

        LeaseRelationships lease = farmerService.submitLease(
            id, tenantFarmerId, landProfileId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(LeaseResponse.from(lease)));
    }

    // ─────────────────────────────────────────────────────
    // GET /api/v1/farmers/{id}/lease
    // Protected — get active lease for this farmer
    // ─────────────────────────────────────────────────────
    @GetMapping("/{id}/lease")
    public ResponseEntity<ApiResponse<FarmerResponse>> getFarmerWithLand(
            @PathVariable UUID id) {

        FarmerResponse response =
            farmerService.getFarmerById(id);
        return ResponseEntity.ok(
            ApiResponse.success(response));
    }
}