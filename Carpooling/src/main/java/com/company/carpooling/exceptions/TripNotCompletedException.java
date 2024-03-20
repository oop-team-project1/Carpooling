package com.company.carpooling.exceptions;

public class TripNotCompletedException extends RuntimeException {
    public TripNotCompletedException(String message) {
        super(message);
    }
}
