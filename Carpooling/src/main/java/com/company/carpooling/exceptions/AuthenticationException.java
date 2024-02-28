package com.company.carpooling.exceptions;

//TODO HttpStatus.Unauthorized (401)
public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String message) {
        super(message);
    }
}
