package ru.liga.petClinic.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class PatientConflictException extends ResponseStatusException {
    public PatientConflictException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
