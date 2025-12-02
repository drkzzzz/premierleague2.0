package com.premier.league.premier_backend.repository;

import com.premier.league.premier_backend.model.FavoritoJugador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoritoJugadorRepository extends JpaRepository<FavoritoJugador, Long> {
    List<FavoritoJugador> findByUsuarioIdUsuario(Long idUsuario);

    Optional<FavoritoJugador> findByUsuarioIdUsuarioAndIdJugadorApi(Long idUsuario, String idJugadorApi);

    void deleteByUsuarioIdUsuarioAndIdJugadorApi(Long idUsuario, String idJugadorApi);
}
