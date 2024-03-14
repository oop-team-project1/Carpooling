package com.company.carpooling.exceptions;

public class UserIsNotFromTrip extends RuntimeException {
    public UserIsNotFromTrip(String message) {
        super(message);
    }
}
