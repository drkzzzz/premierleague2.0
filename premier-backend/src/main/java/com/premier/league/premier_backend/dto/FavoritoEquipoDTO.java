package com.premier.league.premier_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoritoEquipoDTO {
    private Long idFavorito;
    private Long idUsuario;
    private String idEquipoApi;
    private LocalDateTime fechaAdicion;
}
