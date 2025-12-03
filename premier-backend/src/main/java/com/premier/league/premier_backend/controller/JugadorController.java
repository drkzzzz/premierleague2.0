package com.premier.league.premier_backend.controller;

import com.premier.league.premier_backend.dto.JugadorDTO;
import com.premier.league.premier_backend.service.JugadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/jugadores")
@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:5173", "*" })
public class JugadorController {

    @Autowired
    private JugadorService jugadorService;

    /**
     * Obtener todos los jugadores de la Premier League
     * GET /api/jugadores
     */
    @GetMapping
    public ResponseEntity<List<JugadorDTO>> obtenerTodos() {
        try {
            List<JugadorDTO> jugadores = jugadorService.obtenerTodosLosJugadores();
            return ResponseEntity.ok(jugadores);
        } catch (Exception e) {
            System.err.println("Error al obtener jugadores: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Obtener jugadores por equipo
     * GET /api/jugadores/equipo/{equipoId}
     */
    @GetMapping("/equipo/{equipoId}")
    public ResponseEntity<List<JugadorDTO>> obtenerPorEquipo(@PathVariable String equipoId) {
        try {
            List<JugadorDTO> jugadores = jugadorService.obtenerJugadoresPorEquipo(equipoId);
            return ResponseEntity.ok(jugadores);
        } catch (Exception e) {
            System.err.println("Error al obtener jugadores del equipo: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Obtener jugador por ID
     * GET /api/jugadores/{jugadorId}
     */
    @GetMapping("/{jugadorId}")
    public ResponseEntity<Map<String, Object>> obtenerPorId(@PathVariable String jugadorId) {
        try {
            JugadorDTO jugador = jugadorService.obtenerJugadorPorId(jugadorId);

            Map<String, Object> response = new HashMap<>();
            if (jugador != null) {
                response.put("success", true);
                response.put("data", jugador);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Jugador no encontrado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
