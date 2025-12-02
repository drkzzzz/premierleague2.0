package com.premier.league.premier_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComentarioDTO {
    private Long idComentario;
    private Long idUsuario;
    private String contenido;
    private LocalDateTime fechaCreacion;
    private String entidadId;
    private String entidadTipo;
}
