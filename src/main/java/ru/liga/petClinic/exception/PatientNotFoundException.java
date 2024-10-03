package ru.liga.petClinic.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class PatientNotFoundException extends ResponseStatusException {
    public PatientNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
