package com.company.carpooling.exceptions;

public class FullyBookedException extends RuntimeException {

    public FullyBookedException(int id) {
        super(String.format("There are no free spots left on trip with id %s", id));
    }

    public FullyBookedException(String message) {
        super(message);
    }
}
