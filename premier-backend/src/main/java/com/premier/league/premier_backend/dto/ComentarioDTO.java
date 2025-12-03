package com.premier.league.premier_backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ComentarioDTO {
    private Long idComentario;
    private Long idUsuario;
    private String contenido;
    private LocalDateTime fechaCreacion;
    private String entidadId;
    private String entidadTipo;

    // Constructor explícito
    public ComentarioDTO(Long idComentario, Long idUsuario, String contenido, LocalDateTime fechaCreacion,
            String entidadId, String entidadTipo) {
        this.idComentario = idComentario;
        this.idUsuario = idUsuario;
        this.contenido = contenido;
        this.fechaCreacion = fechaCreacion;
        this.entidadId = entidadId;
        this.entidadTipo = entidadTipo;
    }

    public Long getIdComentario() {
        return this.idComentario;
    }

    public void setIdComentario(Long idComentario) {
        this.idComentario = idComentario;
    }

    public Long getIdUsuario() {
        return this.idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getContenido() {
        return this.contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public LocalDateTime getFechaCreacion() {
        return this.fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getEntidadId() {
        return this.entidadId;
    }

    public void setEntidadId(String entidadId) {
        this.entidadId = entidadId;
    }

    public String getEntidadTipo() {
        return this.entidadTipo;
    }

    public void setEntidadTipo(String entidadTipo) {
        this.entidadTipo = entidadTipo;
    }
}
