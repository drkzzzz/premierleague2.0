package com.premier.league.premier_backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class FootballApiService {

    private static final Logger logger = LoggerFactory.getLogger(FootballApiService.class);

    @Value("${api.football.base-url}")
    private String baseUrl;

    @Value("${api.football.key}")
    private String apiKey;

    @Value("${api.premier.league-id}")
    private String leagueId;

    @Value("${api.premier.season}")
    private String season;

    private final RestTemplate restTemplate = new RestTemplate();

    // Método genérico para hacer peticiones a la API externa (football-data.org)
    private String callExternalApi(String endpoint, String params) {
        // Construction de URL para football-data.org
        // Formato: https://api.football-data.org/v4/competitions/{leagueId}/standings
        String url = String.format("%s/competitions/%s/%s%s",
                baseUrl, leagueId, endpoint, params);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Auth-Token", apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            logger.info("Llamando a API externa: {}", url);
            logger.info("API Key enviada: {}", apiKey.substring(0, Math.min(10, apiKey.length())) + "...");
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    String.class);

            if (response.getBody() != null) {
                logger.info("Respuesta exitosa de la API externa");
                logger.debug("Respuesta JSON: {}",
                        response.getBody().substring(0, Math.min(500, response.getBody().length())));
                return response.getBody();
            } else {
                logger.warn("Respuesta vacía de la API externa");
                return null;
            }
        } catch (RestClientException e) {
            logger.error("Error de conexión con API externa: {}", e.getMessage(), e);
            logger.warn("Retornando datos mock para desarrollo");
            return getMockData(endpoint);
        } catch (Exception e) {
            logger.error("Error inesperado al llamar a la API externa: {}", e.getMessage(), e);
            logger.warn("Retornando datos mock para desarrollo");
            return getMockData(endpoint);
        }
    }

    // Datos mock para desarrollo cuando falla la API externa
    private String getMockData(String endpoint) {
        if (endpoint.contains("standings")) {
            return """
                    {
                      "standings": [{
                        "stage": "REGULAR_SEASON",
                        "type": "TOTAL",
                        "group": null,
                        "table": [
                          {"position": 1, "team": {"id": 57, "name": "Arsenal FC", "crest": "https://crests.football-data.org/57.svg"}, "points": 35, "playedGames": 12, "won": 11, "draw": 2, "lost": 0, "goalsFor": 35, "goalsAgainst": 10, "goalDifference": 25},
                          {"position": 2, "team": {"id": 50, "name": "Manchester City FC", "crest": "https://crests.football-data.org/50.svg"}, "points": 34, "playedGames": 12, "won": 11, "draw": 1, "lost": 0, "goalsFor": 32, "goalsAgainst": 8, "goalDifference": 24},
                          {"position": 3, "team": {"id": 64, "name": "Liverpool FC", "crest": "https://crests.football-data.org/64.svg"}, "points": 31, "playedGames": 12, "won": 10, "draw": 1, "lost": 1, "goalsFor": 28, "goalsAgainst": 12, "goalDifference": 16}
                        ]
                      }]
                    }
                    """;
        } else if (endpoint.contains("matches")) {
            return """
                    {
                      "matches": [
                        {
                          "id": 401426,
                          "utcDate": "2025-12-06T15:00:00Z",
                          "status": "SCHEDULED",
                          "matchday": 16,
                          "homeTeam": {"id": 50, "name": "Manchester City FC", "crest": "https://crests.football-data.org/50.svg"},
                          "awayTeam": {"id": 64, "name": "Liverpool FC", "crest": "https://crests.football-data.org/64.svg"},
                          "score": {"fullTime": {"home": null, "away": null}}
                        },
                        {
                          "id": 401427,
                          "utcDate": "2025-12-06T17:30:00Z",
                          "status": "SCHEDULED",
                          "matchday": 16,
                          "homeTeam": {"id": 57, "name": "Arsenal FC", "crest": "https://crests.football-data.org/57.svg"},
                          "awayTeam": {"id": 49, "name": "Chelsea FC", "crest": "https://crests.football-data.org/49.svg"},
                          "score": {"fullTime": {"home": null, "away": null}}
                        }
                      ]
                    }
                    """;
        } else if (endpoint.contains("scorers")) {
            return """
                    {
                      "scorers": [
                        {"player": {"id": 1, "name": "Erling Haaland", "position": "STRIKER"}, "team": {"id": 50, "name": "Manchester City FC", "crest": "https://crests.football-data.org/50.svg"}, "goals": 15, "assists": 3, "penalties": 2},
                        {"player": {"id": 2, "name": "Bukayo Saka", "position": "RIGHT_WING"}, "team": {"id": 57, "name": "Arsenal FC", "crest": "https://crests.football-data.org/57.svg"}, "goals": 12, "assists": 4, "penalties": 0},
                        {"player": {"id": 3, "name": "Mohamed Salah", "position": "RIGHT_WING"}, "team": {"id": 64, "name": "Liverpool FC", "crest": "https://crests.football-data.org/64.svg"}, "goals": 11, "assists": 5, "penalties": 1},
                        {"player": {"id": 4, "name": "Phil Foden", "position": "LEFT_WING"}, "team": {"id": 50, "name": "Manchester City FC", "crest": "https://crests.football-data.org/50.svg"}, "goals": 9, "assists": 3, "penalties": 0}
                      ]
                    }
                    """;
        }
        return null;
    }

    // --- Métodos Específicos para cada vista del Frontend ---

    public String getStandings() {
        logger.info("Obteniendo tabla de posiciones...");
        return callExternalApi("standings", "");
    }

    public String getFixturesByRound(String round) {
        logger.info("Obteniendo partidos para ronda: {}", round);
        // football-data.org usa matchday para filtrar por jornada
        // Extrae el número de jornada del parámetro (ej: "Matchday 8" -> "8")
        String matchdayNumber = round.replaceAll("\\D+", "");
        if (matchdayNumber.isEmpty()) {
            matchdayNumber = "1";
        }
        return callExternalApi("matches", "?matchday=" + matchdayNumber);
    }

    public String getFinishedResults(String round) {
        logger.info("Obteniendo resultados finalizados para ronda: {}", round);
        // Obtiene partidos finalizados, filtrando por jornada si se proporciona
        if (round != null && !round.isEmpty()) {
            String matchdayNumber = round.replaceAll("\\D+", "");
            if (!matchdayNumber.isEmpty()) {
                return callExternalApi("matches", "?status=FINISHED&matchday=" + matchdayNumber);
            }
        }
        return callExternalApi("matches", "?status=FINISHED");
    }

    public String getTopScorers() {
        logger.info("Obteniendo goleadores...");
        // football-data.org retorna los scorers (goleadores) de la competición
        return callExternalApi("scorers", "");
    }
}