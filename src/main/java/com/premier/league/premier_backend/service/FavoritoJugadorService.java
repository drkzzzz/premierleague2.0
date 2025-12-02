package com.premier.league.premier_backend.service;

import com.premier.league.premier_backend.dto.FavoritoJugadorDTO;
import com.premier.league.premier_backend.exception.UsuarioNotFoundException;
import com.premier.league.premier_backend.model.FavoritoJugador;
import com.premier.league.premier_backend.model.Usuario;
import com.premier.league.premier_backend.repository.FavoritoJugadorRepository;
import com.premier.league.premier_backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoritoJugadorService {

    @Autowired
    private FavoritoJugadorRepository favoritoJugadorRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public FavoritoJugadorDTO agregarFavorito(Long idUsuario, String idJugadorApi) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuario no encontrado"));

        // Verificar si ya existe el favorito
        if (favoritoJugadorRepository.findByUsuarioIdUsuarioAndIdJugadorApi(idUsuario, idJugadorApi).isPresent()) {
            throw new IllegalArgumentException("El jugador ya está en favoritos");
        }

        FavoritoJugador favorito = new FavoritoJugador();
        favorito.setUsuario(usuario);
        favorito.setIdJugadorApi(idJugadorApi);
        favorito.setFechaAdicion(LocalDateTime.now());

        FavoritoJugador favoritoGuardado = favoritoJugadorRepository.save(favorito);
        return convertirADTO(favoritoGuardado);
    }

    public List<FavoritoJugadorDTO> obtenerFavoritosPorUsuario(Long idUsuario) {
        return favoritoJugadorRepository.findByUsuarioIdUsuario(idUsuario).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public void eliminarFavorito(Long idUsuario, String idJugadorApi) {
        favoritoJugadorRepository.deleteByUsuarioIdUsuarioAndIdJugadorApi(idUsuario, idJugadorApi);
    }

    private FavoritoJugadorDTO convertirADTO(FavoritoJugador favorito) {
        return new FavoritoJugadorDTO(
                favorito.getIdFavorito(),
                favorito.getUsuario().getIdUsuario(),
                favorito.getIdJugadorApi(),
                favorito.getFechaAdicion());
    }
}
