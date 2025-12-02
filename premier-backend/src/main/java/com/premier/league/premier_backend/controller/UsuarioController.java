package com.premier.league.premier_backend.controller;

import com.premier.league.premier_backend.dto.UsuarioDTO;
import com.premier.league.premier_backend.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:5173", "*" })
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> obtenerTodos() {
        try {
            List<UsuarioDTO> usuarios = usuarioService.obtenerTodos();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", usuarios);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/{idUsuario}")
    public ResponseEntity<Map<String, Object>> obtenerPorId(@PathVariable Long idUsuario) {
        try {
            UsuarioDTO usuario = usuarioService.obtenerPorId(idUsuario);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", usuario);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Map<String, Object>> obtenerPorEmail(@PathVariable String email) {
        try {
            UsuarioDTO usuario = usuarioService.obtenerPorEmail(email);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", usuario);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PutMapping("/{idUsuario}")
    public ResponseEntity<Map<String, Object>> actualizar(@PathVariable Long idUsuario,
            @RequestBody UsuarioDTO usuarioDTO) {
        try {
            UsuarioDTO usuarioActualizado = usuarioService.actualizarUsuario(idUsuario, usuarioDTO);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Usuario actualizado exitosamente");
            response.put("data", usuarioActualizado);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PostMapping("/{idUsuario}/cambiar-contrasena")
    public ResponseEntity<Map<String, Object>> cambiarContrasena(@PathVariable Long idUsuario,
            @RequestBody Map<String, String> request) {
        try {
            String contrasenaActual = request.get("contrasenaActual");
            String contrasenaNueva = request.get("contrasenaNueva");

            usuarioService.cambiarContrasena(idUsuario, contrasenaActual, contrasenaNueva);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Contraseña cambiada exitosamente");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @DeleteMapping("/{idUsuario}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable Long idUsuario) {
        try {
            usuarioService.eliminarUsuario(idUsuario);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Usuario eliminado exitosamente");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
