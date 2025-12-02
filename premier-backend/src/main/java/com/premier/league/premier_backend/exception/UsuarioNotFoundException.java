package com.premier.league.premier_backend.exception;

public class UsuarioNotFoundException extends RuntimeException {
    public UsuarioNotFoundException(String mensaje) {
        super(mensaje);
    }
}
