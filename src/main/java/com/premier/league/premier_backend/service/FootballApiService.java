package com.premier.league.premier_backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FootballApiService {

    @Value("${api.football.base-url}")
    private String baseUrl;

    @Value("${api.football.key}")
    private String apiKey;

    @Value("${api.premier.league-id}")
    private String leagueId;

    @Value("${api.premier.season}")
    private String season;

    private final RestTemplate restTemplate = new RestTemplate();

    // Método genérico para hacer peticiones a la API externa
    private String callExternalApi(String endpoint, String params) {
        String url = String.format("%s/%s?league=%s&season=%s%s",
                baseUrl, endpoint, leagueId, season, params);

        HttpHeaders headers = new HttpHeaders();
        // La mayoría de las APIs deportivas requieren la clave en la cabecera
        headers.set("x-apisports-key", apiKey);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    String.class);
            return response.getBody();
        } catch (Exception e) {
            // Manejar errores de conexión o API
            System.err.println("Error al llamar a la API externa: " + e.getMessage());
            return null;
        }
    }

    // --- Métodos Específicos para cada vista del Frontend ---

    public String getStandings() {
        // Ejemplo de endpoint: /standings
        return callExternalApi("standings", "");
    }

    public String getFixturesByRound(String round) {
        // Ejemplo de endpoint: /fixtures?round=Matchday 8
        return callExternalApi("fixtures", "&round=" + round);
    }

    public String getFinishedResults(String round) {
        // Endpoint para una jornada terminada: /fixtures?round=Matchday 7&status=FT
        return callExternalApi("fixtures", "&round=" + round + "&status=FT");
    }

    public String getTopScorers() {
        // Ejemplo de endpoint: /topscorers
        return callExternalApi("players/topscorers", "");
    }
}