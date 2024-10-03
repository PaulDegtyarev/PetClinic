package ru.liga.petClinic.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class PatientBadRequestException extends ResponseStatusException {
    public PatientBadRequestException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
