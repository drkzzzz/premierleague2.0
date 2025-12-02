package com.premier.league.premier_backend.exception;

public class RolNotFoundException extends RuntimeException {
    public RolNotFoundException(String mensaje) {
        super(mensaje);
    }
}
