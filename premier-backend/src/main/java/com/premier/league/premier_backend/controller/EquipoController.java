package com.premier.league.premier_backend.controller;

import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/equipos")
@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:5173", "*" })
public class EquipoController {

    /**
     * Obtener todos los equipos de la Premier League (datos mock)
     */
    @GetMapping
    public List<Map<String, Object>> obtenerEquipos() {
        List<Map<String, Object>> equipos = new ArrayList<>();

        String[][] teamsData = {
                { "1", "Arsenal", "../img/escudos/arsenal.svg" },
                { "2", "Aston Villa", "../img/escudos/villa.svg" },
                { "3", "AFC Bournemouth", "../img/escudos/bournemouth.svg" },
                { "4", "Brentford", "../img/escudos/brentford.svg" },
                { "5", "Brighton & Hove Albion", "../img/escudos/brighton.svg" },
                { "6", "Chelsea", "../img/escudos/chelsea.svg" },
                { "7", "Crystal Palace", "../img/escudos/palace.svg" },
                { "8", "Everton", "../img/escudos/everton.svg" },
                { "9", "Fulham", "../img/escudos/fulham.svg" },
                { "10", "Ipswich Town", "../img/escudos/leeds.svg" },
                { "11", "Leicester City", "../img/escudos/brighton.svg" },
                { "12", "Liverpool", "../img/escudos/liverpool.svg" },
                { "13", "Manchester City", "../img/escudos/man-city.svg" },
                { "14", "Manchester United", "../img/escudos/man-utd.svg" },
                { "15", "Newcastle United", "../img/escudos/newcastle.svg" },
                { "16", "Nottingham Forest", "../img/escudos/nottingham.svg" },
                { "17", "Southampton", "../img/escudos/brighton.svg" },
                { "18", "Tottenham Hotspur", "../img/escudos/tottenham.svg" },
                { "19", "West Ham United", "../img/escudos/west-ham.svg" },
                { "20", "Wolverhampton Wanderers", "../img/escudos/Wolves.svg" }
        };

        for (String[] team : teamsData) {
            Map<String, Object> equipo = new HashMap<>();
            equipo.put("id", team[0]);
            equipo.put("name", team[1]);
            equipo.put("nombre", team[1]);
            equipo.put("crest", team[2]);
            equipo.put("escudo", team[2]);
            equipos.add(equipo);
        }

        return equipos;
    }

    /**
     * Obtener equipo por ID
     */
    @GetMapping("/{equipoId}")
    public Map<String, Object> obtenerEquipo(@PathVariable String equipoId) {
        List<Map<String, Object>> equipos = obtenerEquipos();
        for (Map<String, Object> equipo : equipos) {
            if (equipo.get("id").toString().equals(equipoId)) {
                return equipo;
            }
        }
        return new HashMap<>();
    }
}
