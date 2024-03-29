package com.company.carpooling.exceptions;

public class BlockedUnblockedUserException extends RuntimeException{
    public BlockedUnblockedUserException(int attribute, String value) {
        super(String.format("User with id %s is already %s.", String.valueOf(attribute), value));
    }
}
