package com.premier.league.premier_backend.controller;

import com.premier.league.premier_backend.service.FootballApiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/datos-deportivos")
public class DatosDeportivosController {

    private final FootballApiService footballApiService;

    public DatosDeportivosController(FootballApiService footballApiService) {
        this.footballApiService = footballApiService;
    }

    @GetMapping("/clasificacion") // Endpoint para tabla.html
    public ResponseEntity<String> getStandings() {
        String data = footballApiService.getStandings();
        if (data != null) {
            return ResponseEntity.ok(data);
        }
        return ResponseEntity.status(503).body("{\"error\": \"No se pudieron obtener datos de la API externa\"}");
    }

    @GetMapping("/jornadas/{round}") // Endpoint para jornadas.html
    public ResponseEntity<String> getFixtures(@PathVariable String round) {
        // round vendría como 'Matchday 8'
        String data = footballApiService.getFixturesByRound(round);
        if (data != null) {
            return ResponseEntity.ok(data);
        }
        return ResponseEntity.status(503).body("{\"error\": \"No se pudieron obtener datos de la API externa\"}");
    }

    @GetMapping("/resultados/{round}") // Endpoint para resultados.html
    public ResponseEntity<String> getResults(@PathVariable String round) {
        // round vendría como 'Matchday 7'
        String data = footballApiService.getFinishedResults(round);
        if (data != null) {
            return ResponseEntity.ok(data);
        }
        return ResponseEntity.status(503).body("{\"error\": \"No se pudieron obtener datos de la API externa\"}");
    }

    @GetMapping("/estadisticas/goleadores") // Endpoint para estadisticas.html
    public ResponseEntity<String> getTopScorers() {
        String data = footballApiService.getTopScorers();
        if (data != null) {
            return ResponseEntity.ok(data);
        }
        return ResponseEntity.status(503).body("{\"error\": \"No se pudieron obtener datos de la API externa\"}");
    }
}