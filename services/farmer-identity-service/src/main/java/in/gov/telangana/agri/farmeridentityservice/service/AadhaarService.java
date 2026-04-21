package in.gov.telangana.agri.farmeridentityservice.service;

public interface AadhaarService {

    AadhaarOtpResult sendOtp(String aadhaarNumber);

    boolean verifyOtp(String aadhaarNumber, String otp, String transactionId);

    record AadhaarOtpResult(
        boolean success,
        String transactionId,
        String maskedMobile,
        String errorMessage
    ) {}
}
