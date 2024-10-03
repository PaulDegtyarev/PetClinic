package ru.liga.petClinic.config;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.liga.petClinic.exception.PatientBadRequestException;
import ru.liga.petClinic.exception.PatientConflictException;
import ru.liga.petClinic.exception.PatientNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(PatientBadRequestException.class)
    public ResponseEntity<String> handlePatientBadRequestException(PatientBadRequestException patientBadRequestException) {
        return ResponseEntity.status(patientBadRequestException.getStatusCode())
                .body(patientBadRequestException.getMessage());
    }

    @ExceptionHandler(PatientNotFoundException.class)
    public ResponseEntity<String> handlePatientNotFoundException(PatientNotFoundException patientNotFoundException) {
        return ResponseEntity.status(patientNotFoundException.getStatusCode())
                .body(patientNotFoundException.getMessage());
    }

    @ExceptionHandler(PatientConflictException.class)
    public ResponseEntity<String> handlePatientConflictException(PatientConflictException patientConflictException) {
        return ResponseEntity.status(patientConflictException.getStatusCode())
                .body(patientConflictException.getMessage());
    }
}
