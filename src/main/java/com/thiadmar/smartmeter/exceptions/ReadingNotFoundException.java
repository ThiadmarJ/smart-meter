package com.thiadmar.smartmeter.exceptions;

public class ReadingNotFoundException extends RuntimeException {
    public ReadingNotFoundException(String message) {
        super(message);
    }
}
