package com.backendapi.exceptions;

public class PokemonException extends RuntimeException {
    public PokemonException() {
        super("Pokemon not found in User data");
    }
}
