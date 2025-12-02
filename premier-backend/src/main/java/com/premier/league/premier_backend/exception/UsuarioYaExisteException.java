package com.premier.league.premier_backend.exception;

public class UsuarioYaExisteException extends RuntimeException {
    public UsuarioYaExisteException(String mensaje) {
        super(mensaje);
    }
}
