package com.premier.league.premier_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "favoritos_equipo", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "id_usuario", "id_equipo_api" })
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoritoEquipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_favorito")
    private Long idFavorito;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(nullable = false, length = 20)
    private String idEquipoApi;

    @Column(name = "fecha_adicion", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime fechaAdicion;

    @PrePersist
    protected void onCreate() {
        if (fechaAdicion == null) {
            fechaAdicion = LocalDateTime.now();
        }
    }
}
