package in.gov.telangana.agri.farmeridentityservice.exception;

import in.gov.telangana.agri.farmeridentityservice.dto.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ApiResponse.<Map<String, String>>builder()
                .success(false)
                .message("Validation failed")
                .data(errors)
                .build()
        );
    }

    @ExceptionHandler(FarmerNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleFarmerNotFound(FarmerNotFoundException ex) {
        log.warn("Farmer not found: {}", ex.getMessage());
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ApiResponse.failure("Farmer not found"));
    }

    @ExceptionHandler(DuplicateEnrollmentException.class)
    public ResponseEntity<ApiResponse<Void>> handleDuplicateEnrollment(DuplicateEnrollmentException ex) {
        log.warn("Duplicate enrollment attempt: {}", ex.getMessage());
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(ApiResponse.failure("Duplicate enrollment"));
    }

    @ExceptionHandler(AadhaarVerificationException.class)
    public ResponseEntity<ApiResponse<Void>> handleAadhaarVerification(AadhaarVerificationException ex) {
        log.warn("Aadhaar verification failed: {}", ex.getMessage());
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.failure("Aadhaar verification failed"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneralException(Exception ex) {
        log.error("An unexpected error occurred: {}", ex.getMessage(), ex);
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponse.failure("An unexpected error occurred"));
    }
}
