package com.premier.league.premier_backend.service;

import com.premier.league.premier_backend.dto.RolDTO;
import com.premier.league.premier_backend.exception.RolNotFoundException;
import com.premier.league.premier_backend.model.Rol;
import com.premier.league.premier_backend.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RolService {

    @Autowired
    private RolRepository rolRepository;

    public RolDTO crearRol(RolDTO rolDTO) {
        Rol rol = new Rol();
        rol.setNombre(rolDTO.getNombre());
        Rol rolGuardado = rolRepository.save(rol);
        return convertirADTO(rolGuardado);
    }

    public List<RolDTO> obtenerTodos() {
        return rolRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public RolDTO obtenerPorId(Long idRol) {
        Rol rol = rolRepository.findById(idRol)
                .orElseThrow(() -> new RolNotFoundException("Rol no encontrado con ID: " + idRol));
        return convertirADTO(rol);
    }

    public RolDTO obtenerPorNombre(String nombre) {
        Rol rol = rolRepository.findByNombre(nombre)
                .orElseThrow(() -> new RolNotFoundException("Rol no encontrado con nombre: " + nombre));
        return convertirADTO(rol);
    }

    public RolDTO actualizarRol(Long idRol, RolDTO rolDTO) {
        Rol rol = rolRepository.findById(idRol)
                .orElseThrow(() -> new RolNotFoundException("Rol no encontrado con ID: " + idRol));
        rol.setNombre(rolDTO.getNombre());
        Rol rolActualizado = rolRepository.save(rol);
        return convertirADTO(rolActualizado);
    }

    public void eliminarRol(Long idRol) {
        if (!rolRepository.existsById(idRol)) {
            throw new RolNotFoundException("Rol no encontrado con ID: " + idRol);
        }
        rolRepository.deleteById(idRol);
    }

    private RolDTO convertirADTO(Rol rol) {
        return new RolDTO(rol.getIdRol(), rol.getNombre());
    }
}
