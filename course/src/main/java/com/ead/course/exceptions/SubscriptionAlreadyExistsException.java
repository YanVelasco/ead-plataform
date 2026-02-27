package com.ead.course.exceptions;

public class SubscriptionAlreadyExistsException extends RuntimeException{
    public SubscriptionAlreadyExistsException(String message) {
        super(message);
    }
}
