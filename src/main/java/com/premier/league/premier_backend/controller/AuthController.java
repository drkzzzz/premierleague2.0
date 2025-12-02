package com.premier.league.premier_backend.controller;

import com.premier.league.premier_backend.dto.*;
import com.premier.league.premier_backend.exception.ContrasenaInvalidaException;
import com.premier.league.premier_backend.exception.UsuarioNotFoundException;
import com.premier.league.premier_backend.model.Usuario;
import com.premier.league.premier_backend.repository.SesionRepository;
import com.premier.league.premier_backend.security.JwtTokenProvider;
import com.premier.league.premier_backend.service.UsuarioService;
import com.premier.league.premier_backend.model.Sesion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:5173", "*" })
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SesionRepository sesionRepository;

    @PostMapping("/registro")
    public ResponseEntity<Map<String, Object>> registro(@RequestBody RegistroDTO registroDTO) {
        try {
            UsuarioDTO usuarioDTO = usuarioService.registrarUsuario(registroDTO);

            String accessToken = jwtTokenProvider.generarAccessToken(usuarioDTO.getNombreUsuario(),
                    usuarioDTO.getIdUsuario());
            String refreshToken = jwtTokenProvider.generarRefreshToken(usuarioDTO.getNombreUsuario(),
                    usuarioDTO.getIdUsuario());

            // Guardar sesión con refresh token
            Usuario usuario = usuarioService.obtenerUsuarioEntity(usuarioDTO.getIdUsuario());
            Sesion sesion = new Sesion();
            sesion.setUsuario(usuario);
            sesion.setTokenRefresh(refreshToken);
            sesion.setFechaExpiracion(LocalDateTime.now().plusDays(7));
            sesionRepository.save(sesion);

            AuthResponseDTO authResponse = new AuthResponseDTO(
                    accessToken,
                    refreshToken,
                    usuarioDTO.getIdUsuario(),
                    usuarioDTO.getNombreUsuario(),
                    usuarioDTO.getEmail());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Usuario registrado exitosamente");
            response.put("data", authResponse);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginDTO loginDTO) {
        try {
            Usuario usuario = usuarioService.obtenerUsuarioEntityPorNombre(loginDTO.getNombreUsuario());

            if (!passwordEncoder.matches(loginDTO.getPassword(), usuario.getPasswordHash())) {
                throw new ContrasenaInvalidaException("Contraseña incorrecta");
            }

            String accessToken = jwtTokenProvider.generarAccessToken(usuario.getNombreUsuario(),
                    usuario.getIdUsuario());
            String refreshToken = jwtTokenProvider.generarRefreshToken(usuario.getNombreUsuario(),
                    usuario.getIdUsuario());

            // Guardar sesión con refresh token
            Sesion sesion = new Sesion();
            sesion.setUsuario(usuario);
            sesion.setTokenRefresh(refreshToken);
            sesion.setFechaExpiracion(LocalDateTime.now().plusDays(7));
            sesionRepository.save(sesion);

            AuthResponseDTO authResponse = new AuthResponseDTO(
                    accessToken,
                    refreshToken,
                    usuario.getIdUsuario(),
                    usuario.getNombreUsuario(),
                    usuario.getEmail());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Login exitoso");
            response.put("data", authResponse);

            return ResponseEntity.ok(response);
        } catch (UsuarioNotFoundException | ContrasenaInvalidaException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Usuario o contraseña incorrectos");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, Object>> refreshToken(@RequestBody Map<String, String> request) {
        try {
            String refreshToken = request.get("refreshToken");

            if (refreshToken == null || !jwtTokenProvider.validarToken(refreshToken)) {
                throw new IllegalArgumentException("Refresh token inválido");
            }

            String nombreUsuario = jwtTokenProvider.obtenerNombreUsuarioDelToken(refreshToken);
            Long idUsuario = jwtTokenProvider.obtenerIdUsuarioDelToken(refreshToken);

            String newAccessToken = jwtTokenProvider.generarAccessToken(nombreUsuario, idUsuario);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("accessToken", newAccessToken);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(@RequestBody Map<String, String> request) {
        try {
            String refreshToken = request.get("refreshToken");
            if (refreshToken != null) {
                sesionRepository.deleteByTokenRefresh(refreshToken);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Logout exitoso");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
