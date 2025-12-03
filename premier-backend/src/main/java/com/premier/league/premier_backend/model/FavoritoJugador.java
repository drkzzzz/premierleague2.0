package com.premier.league.premier_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "favoritos_jugador", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "id_usuario", "id_jugador_api" })
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoritoJugador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_favorito")
    private Long idFavorito;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(nullable = false, length = 20)
    private String idJugadorApi;

    @Column(name = "fecha_adicion", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime fechaAdicion;

    @PrePersist
    protected void onCreate() {
        if (fechaAdicion == null) {
            fechaAdicion = LocalDateTime.now();
        }
    }

    public Long getIdFavorito() {
        return this.idFavorito;
    }

    public void setIdFavorito(Long idFavorito) {
        this.idFavorito = idFavorito;
    }

    public Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
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
