package com.company.carpooling.exceptions;

public class EntityDuplicateException extends RuntimeException{
    public EntityDuplicateException(String type, String attribute, String value) {
        super(String.format("%s with %s %s already exists.", type, attribute, value));
    }
    public EntityDuplicateException(String type, String attribute, int value) {
        super(String.format("%s with %s %d already exists.", type, attribute, value));
    }
}
