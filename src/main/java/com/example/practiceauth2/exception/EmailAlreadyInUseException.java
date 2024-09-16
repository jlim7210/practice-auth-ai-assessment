package com.example.practiceauth2.exception;

public class EmailAlreadyInUseException extends RuntimeException {
    public EmailAlreadyInUseException(String email) {
        super("Email " + email + " is already in use");
    }

    public EmailAlreadyInUseException(String email, Throwable cause) {
        super("Email " + email + " is already in use", cause);
    }

    public EmailAlreadyInUseException(Throwable cause) {
        super(cause);
    }

    public EmailAlreadyInUseException() {
        super("Email is already in use.");
    }

    public EmailAlreadyInUseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
