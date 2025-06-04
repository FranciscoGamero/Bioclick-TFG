package com.salesianostriana.bioclick.security.jwt.refresh.error;

import com.salesianostriana.bioclick.security.exceptionhandling.JwtException;

public class RefreshTokenException extends JwtException {
    public RefreshTokenException(String s) {
        super(s);
    }
}
