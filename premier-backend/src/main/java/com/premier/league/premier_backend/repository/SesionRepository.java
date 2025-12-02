package com.premier.league.premier_backend.repository;

import com.premier.league.premier_backend.model.Sesion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface SesionRepository extends JpaRepository<Sesion, Long> {
    Optional<Sesion> findByTokenRefresh(String tokenRefresh);

    List<Sesion> findByUsuarioIdUsuario(Long idUsuario);

    void deleteByTokenRefresh(String tokenRefresh);
}
