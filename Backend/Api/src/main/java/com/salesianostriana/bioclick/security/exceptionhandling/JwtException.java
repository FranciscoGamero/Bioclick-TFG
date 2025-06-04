package com.salesianostriana.bioclick.security.exceptionhandling;

public class JwtException extends RuntimeException {
    public JwtException(String message) {
        super(message);
    }
}
