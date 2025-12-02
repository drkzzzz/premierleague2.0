package com.premier.league.premier_backend.controller;

import com.premier.league.premier_backend.dto.ComentarioDTO;
import com.premier.league.premier_backend.service.ComentarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comentarios")
@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:5173", "*" })
public class ComentarioController {

    @Autowired
    private ComentarioService comentarioService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> crear(@RequestBody ComentarioDTO comentarioDTO) {
        try {
            ComentarioDTO comentarioCreado = comentarioService.crearComentario(comentarioDTO);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Comentario creado exitosamente");
            response.put("data", comentarioCreado);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("/entidad/{entidadId}/{entidadTipo}")
    public ResponseEntity<Map<String, Object>> obtenerPorEntidad(@PathVariable String entidadId,
            @PathVariable String entidadTipo) {
        try {
            List<ComentarioDTO> comentarios = comentarioService.obtenerPorEntidad(entidadId, entidadTipo);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", comentarios);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<Map<String, Object>> obtenerPorUsuario(@PathVariable Long idUsuario) {
        try {
            List<ComentarioDTO> comentarios = comentarioService.obtenerPorUsuario(idUsuario);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", comentarios);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/{idComentario}")
    public ResponseEntity<Map<String, Object>> obtenerPorId(@PathVariable Long idComentario) {
        try {
            ComentarioDTO comentario = comentarioService.obtenerPorId(idComentario);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", comentario);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @DeleteMapping("/{idComentario}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable Long idComentario) {
        try {
            comentarioService.eliminarComentario(idComentario);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Comentario eliminado exitosamente");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
