package com.premier.league.premier_backend.controller;

import com.premier.league.premier_backend.dto.PermisoDTO;
import com.premier.league.premier_backend.service.PermisoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/permisos")
@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:5173", "*" })
public class PermisoController {

    @Autowired
    private PermisoService permisoService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> crear(@RequestBody PermisoDTO permisoDTO) {
        try {
            PermisoDTO permisoCreado = permisoService.crearPermiso(permisoDTO);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Permiso creado exitosamente");
            response.put("data", permisoCreado);

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
            List<PermisoDTO> permisos = permisoService.obtenerTodos();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", permisos);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/{idPermiso}")
    public ResponseEntity<Map<String, Object>> obtenerPorId(@PathVariable Long idPermiso) {
        try {
            PermisoDTO permiso = permisoService.obtenerPorId(idPermiso);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", permiso);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PutMapping("/{idPermiso}")
    public ResponseEntity<Map<String, Object>> actualizar(@PathVariable Long idPermiso,
            @RequestBody PermisoDTO permisoDTO) {
        try {
            PermisoDTO permisoActualizado = permisoService.actualizarPermiso(idPermiso, permisoDTO);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Permiso actualizado exitosamente");
            response.put("data", permisoActualizado);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @DeleteMapping("/{idPermiso}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable Long idPermiso) {
        try {
            permisoService.eliminarPermiso(idPermiso);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Permiso eliminado exitosamente");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
