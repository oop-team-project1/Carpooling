package com.company.carpooling.exceptions;

public class ForbiddenOperationException extends RuntimeException{
    public ForbiddenOperationException(String msg) {
        super(msg);
    }
}
