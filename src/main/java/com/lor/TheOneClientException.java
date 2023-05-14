package com.lor;

public class TheOneClientException extends RuntimeException{
    public TheOneClientException(String message, Throwable t) {
        super(message, t);
    }
}
