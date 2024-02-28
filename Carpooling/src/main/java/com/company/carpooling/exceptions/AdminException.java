package com.company.carpooling.exceptions;

public class AdminException extends RuntimeException{
    public AdminException(int attribute, String value) {
        super(String.format("User with id %s is already %s.", String.valueOf(attribute), value));
    }
}
