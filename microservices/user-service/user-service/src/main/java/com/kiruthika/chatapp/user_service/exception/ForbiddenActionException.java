package com.kiruthika.chatapp.user_service.exception;

public class ForbiddenActionException extends RuntimeException{
    public ForbiddenActionException(String message) {
        super(message);
    }
}
