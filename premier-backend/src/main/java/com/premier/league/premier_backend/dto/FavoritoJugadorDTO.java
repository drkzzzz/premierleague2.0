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

    // Constructor explícito
    public FavoritoJugadorDTO(Long idFavorito, Long idUsuario, String idJugadorApi, LocalDateTime fechaAdicion) {
        this.idFavorito = idFavorito;
        this.idUsuario = idUsuario;
        this.idJugadorApi = idJugadorApi;
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

    public String getIdJugadorApi() {
        return this.idJugadorApi;
    }

    public void setIdJugadorApi(String idJugadorApi) {
        this.idJugadorApi = idJugadorApi;
    }

    public LocalDateTime getFechaAdicion() {
        return this.fechaAdicion;
    }

    public void setFechaAdicion(LocalDateTime fechaAdicion) {
        this.fechaAdicion = fechaAdicion;
    }
}
