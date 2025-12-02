package com.premier.league.premier_backend.controller;

import com.premier.league.premier_backend.dto.RolDTO;
import com.premier.league.premier_backend.service.RolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/roles")
@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:5173", "*" })
public class RolController {

    @Autowired
    private RolService rolService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> crear(@RequestBody RolDTO rolDTO) {
        try {
            RolDTO rolCreado = rolService.crearRol(rolDTO);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Rol creado exitosamente");
            response.put("data", rolCreado);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> obtenerTodos() {
        try {
            List<RolDTO> roles = rolService.obtenerTodos();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", roles);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/{idRol}")
    public ResponseEntity<Map<String, Object>> obtenerPorId(@PathVariable Long idRol) {
        try {
            RolDTO rol = rolService.obtenerPorId(idRol);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", rol);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PutMapping("/{idRol}")
    public ResponseEntity<Map<String, Object>> actualizar(@PathVariable Long idRol, @RequestBody RolDTO rolDTO) {
        try {
            RolDTO rolActualizado = rolService.actualizarRol(idRol, rolDTO);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Rol actualizado exitosamente");
            response.put("data", rolActualizado);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @DeleteMapping("/{idRol}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable Long idRol) {
        try {
            rolService.eliminarRol(idRol);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Rol eliminado exitosamente");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
