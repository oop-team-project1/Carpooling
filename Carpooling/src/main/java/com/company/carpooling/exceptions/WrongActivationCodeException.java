package com.company.carpooling.exceptions;

public class WrongActivationCodeException extends RuntimeException{
    public WrongActivationCodeException(String msg) {
        super(msg);
    }
}
