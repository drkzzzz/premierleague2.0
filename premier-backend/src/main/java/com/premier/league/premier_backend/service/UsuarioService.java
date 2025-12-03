package com.premier.league.premier_backend.service;

import com.premier.league.premier_backend.dto.UsuarioDTO;
import com.premier.league.premier_backend.dto.RegistroDTO;
import com.premier.league.premier_backend.dto.RolDTO;
import com.premier.league.premier_backend.exception.UsuarioNotFoundException;
import com.premier.league.premier_backend.exception.UsuarioYaExisteException;
import com.premier.league.premier_backend.exception.RolNotFoundException;
import com.premier.league.premier_backend.model.Usuario;
import com.premier.league.premier_backend.model.Rol;
import com.premier.league.premier_backend.repository.UsuarioRepository;
import com.premier.league.premier_backend.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UsuarioDTO registrarUsuario(RegistroDTO registroDTO) {
        // Validar que no exista usuario con el mismo nombre o email
        if (usuarioRepository.existsByNombreUsuario(registroDTO.getNombreUsuario())) {
            throw new UsuarioYaExisteException("El nombre de usuario ya existe");
        }

        if (usuarioRepository.existsByEmail(registroDTO.getEmail())) {
            throw new UsuarioYaExisteException("El email ya existe");
        }

        // Obtener rol USER por defecto
        Rol rolUser = rolRepository.findByNombre("ROLE_USER")
                .orElseThrow(() -> new RolNotFoundException("Rol USER no encontrado"));

        Usuario usuario = new Usuario();
        usuario.setNombreUsuario(registroDTO.getNombreUsuario());
        usuario.setEmail(registroDTO.getEmail());
        usuario.setPasswordHash(passwordEncoder.encode(registroDTO.getPassword()));
        usuario.setRol(rolUser);
        usuario.setFechaRegistro(LocalDateTime.now());

        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        return convertirADTO(usuarioGuardado);
    }

    public UsuarioDTO obtenerPorId(Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuario no encontrado con ID: " + idUsuario));
        return convertirADTO(usuario);
    }

    public UsuarioDTO obtenerPorNombreUsuario(String nombreUsuario) {
        Usuario usuario = usuarioRepository.findByNombreUsuario(nombreUsuario)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuario no encontrado: " + nombreUsuario));
        return convertirADTO(usuario);
    }

    public UsuarioDTO obtenerPorEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuario no encontrado con email: " + email));
        return convertirADTO(usuario);
    }

    public List<UsuarioDTO> obtenerTodos() {
        return usuarioRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<UsuarioDTO> obtenerTodosLosUsuarios() {
        return obtenerTodos();
    }

    public UsuarioDTO obtenerUsuarioPorId(Long idUsuario) {
        return obtenerPorId(idUsuario);
    }

    public UsuarioDTO bloquearUsuario(Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuario no encontrado con ID: " + idUsuario));
        // Aquí iría la lógica de bloqueo si existe un campo 'activo' o 'bloqueado'
        // Por ahora, solo retornamos el usuario
        return convertirADTO(usuario);
    }

    public UsuarioDTO actualizarUsuario(Long idUsuario, UsuarioDTO usuarioDTO) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuario no encontrado con ID: " + idUsuario));

        usuario.setEmail(usuarioDTO.getEmail());

        if (usuarioDTO.getIdRol() != null) {
            Rol rol = rolRepository.findById(usuarioDTO.getIdRol())
                    .orElseThrow(() -> new RolNotFoundException("Rol no encontrado"));
            usuario.setRol(rol);
        }

        Usuario usuarioActualizado = usuarioRepository.save(usuario);
        return convertirADTO(usuarioActualizado);
    }

    public void cambiarContrasena(Long idUsuario, String contrasenaActual, String contrasenaNueva) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuario no encontrado con ID: " + idUsuario));

        if (!passwordEncoder.matches(contrasenaActual, usuario.getPasswordHash())) {
            throw new IllegalArgumentException("Contraseña actual incorrecta");
        }

        usuario.setPasswordHash(passwordEncoder.encode(contrasenaNueva));
        usuarioRepository.save(usuario);
    }

    public void eliminarUsuario(Long idUsuario) {
        if (!usuarioRepository.existsById(idUsuario)) {
            throw new UsuarioNotFoundException("Usuario no encontrado con ID: " + idUsuario);
        }
        usuarioRepository.deleteById(idUsuario);
    }

    public UsuarioDTO crearUsuario(UsuarioDTO usuarioDTO) {
        // Validar que no exista usuario con el mismo nombre o email
        if (usuarioRepository.existsByNombreUsuario(usuarioDTO.getNombreUsuario())) {
            throw new UsuarioYaExisteException("El nombre de usuario ya existe");
        }

        if (usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
            throw new UsuarioYaExisteException("El email ya existe");
        }

        // Obtener rol (por defecto ROLE_USER si no se proporciona)
        Rol rol;
        if (usuarioDTO.getIdRol() != null) {
            rol = rolRepository.findById(usuarioDTO.getIdRol())
                    .orElseThrow(() -> new RolNotFoundException("Rol no encontrado"));
        } else {
            rol = rolRepository.findByNombre("ROLE_USER")
                    .orElseThrow(() -> new RolNotFoundException("Rol USER no encontrado"));
        }

        Usuario usuario = new Usuario();
        usuario.setNombreUsuario(usuarioDTO.getNombreUsuario());
        usuario.setEmail(usuarioDTO.getEmail());
        // Usar una contraseña por defecto o generada
        usuario.setPasswordHash(passwordEncoder.encode("password123"));
        usuario.setRol(rol);
        usuario.setFechaRegistro(LocalDateTime.now());

        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        return convertirADTO(usuarioGuardado);
    }

    public Usuario obtenerUsuarioEntity(Long idUsuario) {
        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuario no encontrado con ID: " + idUsuario));
    }

    public Usuario obtenerUsuarioEntityPorNombre(String nombreUsuario) {
        return usuarioRepository.findByNombreUsuario(nombreUsuario)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuario no encontrado: " + nombreUsuario));
    }

    private UsuarioDTO convertirADTO(Usuario usuario) {
        RolDTO rolDTO = null;
        if (usuario.getRol() != null) {
            rolDTO = new RolDTO(usuario.getRol().getIdRol(), usuario.getRol().getNombre());
        }
        return new UsuarioDTO(
                usuario.getIdUsuario(),
                usuario.getRol() != null ? usuario.getRol().getIdRol() : null,
                usuario.getNombreUsuario(),
                usuario.getEmail(),
                usuario.getFechaRegistro(),
                rolDTO);
    }
}
