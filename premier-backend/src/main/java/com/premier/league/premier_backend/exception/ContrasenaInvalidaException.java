package com.premier.league.premier_backend.exception;

public class ContrasenaInvalidaException extends RuntimeException {
    public ContrasenaInvalidaException(String mensaje) {
        super(mensaje);
    }
}
