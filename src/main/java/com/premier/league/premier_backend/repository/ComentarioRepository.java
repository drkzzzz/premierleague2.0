package com.premier.league.premier_backend.repository;

import com.premier.league.premier_backend.model.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
    List<Comentario> findByEntidadIdAndEntidadTipo(String entidadId, Comentario.TipoEntidad entidadTipo);

    List<Comentario> findByUsuarioIdUsuario(Long idUsuario);

    void deleteByIdComentario(Long idComentario);
}
