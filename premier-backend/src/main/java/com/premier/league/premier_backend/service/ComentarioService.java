package com.premier.league.premier_backend.service;

import com.premier.league.premier_backend.dto.ComentarioDTO;
import com.premier.league.premier_backend.exception.UsuarioNotFoundException;
import com.premier.league.premier_backend.model.Comentario;
import com.premier.league.premier_backend.model.Usuario;
import com.premier.league.premier_backend.repository.ComentarioRepository;
import com.premier.league.premier_backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComentarioService {

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public ComentarioDTO crearComentario(ComentarioDTO comentarioDTO) {
        Usuario usuario = usuarioRepository.findById(comentarioDTO.getIdUsuario())
                .orElseThrow(() -> new UsuarioNotFoundException("Usuario no encontrado"));

        Comentario comentario = new Comentario();
        comentario.setUsuario(usuario);
        comentario.setContenido(comentarioDTO.getContenido());
        comentario.setEntidadId(comentarioDTO.getEntidadId());
        comentario.setEntidadTipo(Comentario.TipoEntidad.valueOf(comentarioDTO.getEntidadTipo()));
        comentario.setFechaCreacion(LocalDateTime.now());

        Comentario comentarioGuardado = comentarioRepository.save(comentario);
        return convertirADTO(comentarioGuardado);
    }

    public List<ComentarioDTO> obtenerPorEntidad(String entidadId, String entidadTipo) {
        Comentario.TipoEntidad tipo = Comentario.TipoEntidad.valueOf(entidadTipo);
        return comentarioRepository.findByEntidadIdAndEntidadTipo(entidadId, tipo).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<ComentarioDTO> obtenerPorUsuario(Long idUsuario) {
        return comentarioRepository.findByUsuarioIdUsuario(idUsuario).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public ComentarioDTO obtenerPorId(Long idComentario) {
        Comentario comentario = comentarioRepository.findById(idComentario)
                .orElseThrow(() -> new IllegalArgumentException("Comentario no encontrado"));
        return convertirADTO(comentario);
    }

    public void eliminarComentario(Long idComentario) {
        comentarioRepository.deleteByIdComentario(idComentario);
    }

    private ComentarioDTO convertirADTO(Comentario comentario) {
        return new ComentarioDTO(
                comentario.getIdComentario(),
                comentario.getUsuario().getIdUsuario(),
                comentario.getContenido(),
                comentario.getFechaCreacion(),
                comentario.getEntidadId(),
                comentario.getEntidadTipo().toString());
    }
}
