package com.premier.league.premier_backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class UsuarioDTO {
    private Long idUsuario;
    private Long idRol;
    private String nombreUsuario;
    private String email;
    private LocalDateTime fechaRegistro;
    private RolDTO rol;

    // Constructor explícito con 6 parámetros (incluyendo rol)
    public UsuarioDTO(Long idUsuario, Long idRol, String nombreUsuario, String email, LocalDateTime fechaRegistro,
            RolDTO rol) {
        this.idUsuario = idUsuario;
        this.idRol = idRol;
        this.nombreUsuario = nombreUsuario;
        this.email = email;
        this.fechaRegistro = fechaRegistro;
        this.rol = rol;
    }

    public Long getIdUsuario() {
        return this.idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Long getIdRol() {
        return this.idRol;
    }

    public void setIdRol(Long idRol) {
        this.idRol = idRol;
    }

    public String getNombreUsuario() {
        return this.nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getFechaRegistro() {
        return this.fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public RolDTO getRol() {
        return this.rol;
    }

    public void setRol(RolDTO rol) {
        this.rol = rol;
    }
}
