package com.premier.league.premier_backend.service;

import com.premier.league.premier_backend.dto.FavoritoEquipoDTO;
import com.premier.league.premier_backend.exception.UsuarioNotFoundException;
import com.premier.league.premier_backend.model.FavoritoEquipo;
import com.premier.league.premier_backend.model.Usuario;
import com.premier.league.premier_backend.repository.FavoritoEquipoRepository;
import com.premier.league.premier_backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoritoEquipoService {

    @Autowired
    private FavoritoEquipoRepository favoritoEquipoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public FavoritoEquipoDTO agregarFavorito(Long idUsuario, String idEquipoApi) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuario no encontrado"));

        // Verificar si ya existe el favorito
        if (favoritoEquipoRepository.findByUsuarioIdUsuarioAndIdEquipoApi(idUsuario, idEquipoApi).isPresent()) {
            throw new IllegalArgumentException("El equipo ya está en favoritos");
        }

        FavoritoEquipo favorito = new FavoritoEquipo();
        favorito.setUsuario(usuario);
        favorito.setIdEquipoApi(idEquipoApi);
        favorito.setFechaAdicion(LocalDateTime.now());

        FavoritoEquipo favoritoGuardado = favoritoEquipoRepository.save(favorito);
        return convertirADTO(favoritoGuardado);
    }

    public List<FavoritoEquipoDTO> obtenerFavoritosPorUsuario(Long idUsuario) {
        return favoritoEquipoRepository.findByUsuarioIdUsuario(idUsuario).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public void eliminarFavorito(Long idUsuario, String idEquipoApi) {
        favoritoEquipoRepository.deleteByUsuarioIdUsuarioAndIdEquipoApi(idUsuario, idEquipoApi);
    }

    private FavoritoEquipoDTO convertirADTO(FavoritoEquipo favorito) {
        return new FavoritoEquipoDTO(
                favorito.getIdFavorito(),
                favorito.getUsuario().getIdUsuario(),
                favorito.getIdEquipoApi(),
                favorito.getFechaAdicion());
    }
}
