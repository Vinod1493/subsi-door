package in.gov.telangana.agri.farmeridentityservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@Profile("local")
public class MockAadhaarService implements AadhaarService {

    // In local profile this OTP always works
    private static final String MOCK_OTP = "123456";

    @Override
    public AadhaarOtpResult sendOtp(String aadhaarNumber) {
        String transactionId = "MOCK-TXN-" + UUID.randomUUID();
        String maskedMobile = "XXXXXXXX" +
            aadhaarNumber.substring(aadhaarNumber.length() - 2);

        log.info("""
            ╔══════════════════════════════════════╗
            ║     MOCK AADHAAR OTP SERVICE         ║
            ║  Aadhaar : {}XXXXXXXX        ║
            ║  OTP     : {}                    ║
            ║  Txn ID  : {}  ║
            ╚══════════════════════════════════════╝
            """,
            aadhaarNumber.substring(0, 4),
            MOCK_OTP,
            transactionId);

        return new AadhaarOtpResult(
            true,
            transactionId,
            maskedMobile,
            null
        );
    }

    @Override
    public boolean verifyOtp(String aadhaarNumber,
                             String otp,
                             String transactionId) {
        boolean valid = MOCK_OTP.equals(otp)
                     && transactionId.startsWith("MOCK-TXN-");

        if (!valid) {
            log.warn("Mock OTP verification failed. " +
                     "Expected: {}, Got: {}", MOCK_OTP, otp);
        }

        return valid;
    }
}