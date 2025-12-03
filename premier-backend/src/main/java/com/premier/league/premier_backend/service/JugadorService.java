package com.premier.league.premier_backend.service;

import com.premier.league.premier_backend.dto.JugadorDTO;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class JugadorService {

    /**
     * Obtener todos los jugadores de la Premier League (datos mock)
     */
    public List<JugadorDTO> obtenerTodosLosJugadores() {
        List<JugadorDTO> jugadores = new ArrayList<>();

        String[][] jugadoresData = {
                { "1", "Erling Haaland", "ST", "9" },
                { "2", "Bukayo Saka", "RW", "7" },
                { "3", "Mohamed Salah", "RW", "11" },
                { "4", "Phil Foden", "LW", "20" },
                { "5", "Harry Kane", "ST", "10" },
                { "6", "Cristiano Ronaldo", "ST", "7" },
                { "7", "Son Heung-min", "LW", "7" },
                { "8", "Antony", "RW", "8" },
                { "9", "Bruno Fernandes", "CM", "8" },
                { "10", "Martin Odegaard", "CM", "8" },
                { "11", "Declan Rice", "CM", "41" },
                { "12", "Jude Bellingham", "CM", "5" },
                { "13", "Vinícius Jr", "LW", "20" },
                { "14", "Rodri", "CM", "16" },
                { "15", "Kevin De Bruyne", "CM", "17" },
                { "16", "Alexander-Arnold", "RB", "66" },
                { "17", "Luke Shaw", "LB", "23" },
                { "18", "Virgil van Dijk", "CB", "4" },
                { "19", "Ruben Dias", "CB", "3" },
                { "20", "Aaron Wan-Bissaka", "RB", "29" },
                { "21", "Ollie Watkins", "ST", "11" },
                { "22", "Jack Grealish", "LW", "10" },
                { "23", "Alexis Mac Allister", "CM", "10" },
                { "24", "Moisés Caicedo", "CM", "25" },
                { "25", "Dominic Solanke", "ST", "19" },
                { "26", "Ryan Sessegnon", "LB", "37" },
                { "27", "Marcus Rashford", "LW", "10" },
                { "28", "Nemanja Matic", "CM", "31" },
                { "29", "Jordan Henderson", "CM", "14" },
                { "30", "Sadio Mané", "LW", "10" }
        };

        for (String[] jugador : jugadoresData) {
            JugadorDTO dto = new JugadorDTO();
            dto.setId(jugador[0]);
            dto.setIdJugadorApi(jugador[0]);
            dto.setNombre(jugador[1]);
            dto.setName(jugador[1]);
            dto.setPosition(jugador[2]);
            dto.setPosicion(jugador[2]);
            try {
                dto.setNumero(Integer.parseInt(jugador[3]));
            } catch (NumberFormatException e) {
                dto.setNumero(0);
            }
            jugadores.add(dto);
        }

        jugadores.sort(Comparator.comparing(JugadorDTO::getNombre));
        return jugadores;
    }

    /**
     * Obtener jugadores por equipo
     */
    public List<JugadorDTO> obtenerJugadoresPorEquipo(String equipoId) {
        return obtenerTodosLosJugadores();
    }

    /**
     * Obtener jugador por ID
     */
    public JugadorDTO obtenerJugadorPorId(String jugadorId) {
        List<JugadorDTO> jugadores = obtenerTodosLosJugadores();
        for (JugadorDTO jugador : jugadores) {
            if (jugador.getId().equals(jugadorId)) {
                return jugador;
            }
        }
        return null;
    }
}
