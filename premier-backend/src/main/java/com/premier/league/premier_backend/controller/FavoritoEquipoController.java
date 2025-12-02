package com.premier.league.premier_backend.controller;

import com.premier.league.premier_backend.dto.FavoritoEquipoDTO;
import com.premier.league.premier_backend.service.FavoritoEquipoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/favoritos/equipos")
@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:5173", "*" })
public class FavoritoEquipoController {

    @Autowired
    private FavoritoEquipoService favoritoEquipoService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> agregarFavorito(@RequestBody Map<String, Object> request) {
        try {
            Long idUsuario = Long.parseLong(request.get("idUsuario").toString());
            String idEquipoApi = request.get("idEquipoApi").toString();

            FavoritoEquipoDTO favorito = favoritoEquipoService.agregarFavorito(idUsuario, idEquipoApi);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Equipo agregado a favoritos");
            response.put("data", favorito);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<Map<String, Object>> obtenerFavoritos(@PathVariable Long idUsuario) {
        try {
            List<FavoritoEquipoDTO> favoritos = favoritoEquipoService.obtenerFavoritosPorUsuario(idUsuario);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", favoritos);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping
    public ResponseEntity<Map<String, Object>> eliminarFavorito(@RequestBody Map<String, Object> request) {
        try {
            Long idUsuario = Long.parseLong(request.get("idUsuario").toString());
            String idEquipoApi = request.get("idEquipoApi").toString();

            favoritoEquipoService.eliminarFavorito(idUsuario, idEquipoApi);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Equipo eliminado de favoritos");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
