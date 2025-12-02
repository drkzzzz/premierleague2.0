package com.premier.league.premier_backend.repository;

import com.premier.league.premier_backend.model.FavoritoEquipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoritoEquipoRepository extends JpaRepository<FavoritoEquipo, Long> {
    List<FavoritoEquipo> findByUsuarioIdUsuario(Long idUsuario);

    Optional<FavoritoEquipo> findByUsuarioIdUsuarioAndIdEquipoApi(Long idUsuario, String idEquipoApi);

    void deleteByUsuarioIdUsuarioAndIdEquipoApi(Long idUsuario, String idEquipoApi);
}
