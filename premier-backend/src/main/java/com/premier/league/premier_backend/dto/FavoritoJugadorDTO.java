package com.premier.league.premier_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoritoJugadorDTO {
    private Long idFavorito;
    private Long idUsuario;
    private String idJugadorApi;
    private LocalDateTime fechaAdicion;
}
