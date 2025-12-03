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

    // Constructor explícito
    public FavoritoEquipoDTO(Long idFavorito, Long idUsuario, String idEquipoApi, LocalDateTime fechaAdicion) {
        this.idFavorito = idFavorito;
        this.idUsuario = idUsuario;
        this.idEquipoApi = idEquipoApi;
        this.fechaAdicion = fechaAdicion;
    }

    public Long getIdFavorito() {
        return this.idFavorito;
    }

    public void setIdFavorito(Long idFavorito) {
        this.idFavorito = idFavorito;
    }

    public Long getIdUsuario() {
        return this.idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getIdEquipoApi() {
        return this.idEquipoApi;
    }

    public void setIdEquipoApi(String idEquipoApi) {
        this.idEquipoApi = idEquipoApi;
    }

    public LocalDateTime getFechaAdicion() {
        return this.fechaAdicion;
    }

    public void setFechaAdicion(LocalDateTime fechaAdicion) {
        this.fechaAdicion = fechaAdicion;
    }
}
