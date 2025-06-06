package com.backendapi.exceptions;

public class PokeApiException extends RuntimeException {
    public PokeApiException(String message) {
        super(message);
    }
}
