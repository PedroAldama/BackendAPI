package com.backendapi.exceptions;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final String ERROR = "error";
    private static final String MESSAGE = "message";

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
        Map<String, String> response = new HashMap<>();
        response.put(ERROR, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler({BadCredentialsException.class, UsernameNotFoundException.class})
    public ResponseEntity<?> handleAuthenticationExceptions(Exception ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put(ERROR, "Unauthorized");
        errorResponse.put(MESSAGE, "Invalid username or password");
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler({BagException.BagNotFoundException.class})
    public ResponseEntity<?> handleBagExceptions(Exception ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put(ERROR, "Bag Not Found");
        errorResponse.put(MESSAGE, "Bag Not Found");
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler({PokeApiException.class})
    public ResponseEntity<?> handlePokeApiException(Exception ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put(ERROR, "Pokemon or Generation not found");
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler({PokemonException.class})
    public ResponseEntity<?> handlePokemonException(Exception ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put(ERROR, "Pokemon not found");
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }


}