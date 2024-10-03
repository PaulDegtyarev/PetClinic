package ru.liga.petClinic.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class FileNotFoundException extends ResponseStatusException {
    public FileNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
