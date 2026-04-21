package in.gov.telangana.agri.farmeridentityservice.exception;

import java.util.UUID;

public class DuplicateEnrollmentException extends RuntimeException {

    public DuplicateEnrollmentException(String aadhaarNumber) {
        super("Farmer with Aadhaar number " + aadhaarNumber.substring(8) + " is already enrolled.");
    }

}
