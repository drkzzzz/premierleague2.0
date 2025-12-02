package com.premier.league.premier_backend.service;

import com.premier.league.premier_backend.dto.PermisoDTO;
import com.premier.league.premier_backend.exception.RolNotFoundException;
import com.premier.league.premier_backend.model.Permiso;
import com.premier.league.premier_backend.repository.PermisoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PermisoService {

    @Autowired
    private PermisoRepository permisoRepository;

    public PermisoDTO crearPermiso(PermisoDTO permisoDTO) {
        Permiso permiso = new Permiso();
        permiso.setNombre(permisoDTO.getNombre());
        Permiso permisoGuardado = permisoRepository.save(permiso);
        return convertirADTO(permisoGuardado);
    }

    public List<PermisoDTO> obtenerTodos() {
        return permisoRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public PermisoDTO obtenerPorId(Long idPermiso) {
        Permiso permiso = permisoRepository.findById(idPermiso)
                .orElseThrow(() -> new RolNotFoundException("Permiso no encontrado con ID: " + idPermiso));
        return convertirADTO(permiso);
    }

    public PermisoDTO obtenerPorNombre(String nombre) {
        Permiso permiso = permisoRepository.findByNombre(nombre)
                .orElseThrow(() -> new RolNotFoundException("Permiso no encontrado con nombre: " + nombre));
        return convertirADTO(permiso);
    }

    public PermisoDTO actualizarPermiso(Long idPermiso, PermisoDTO permisoDTO) {
        Permiso permiso = permisoRepository.findById(idPermiso)
                .orElseThrow(() -> new RolNotFoundException("Permiso no encontrado con ID: " + idPermiso));
        permiso.setNombre(permisoDTO.getNombre());
        Permiso permisoActualizado = permisoRepository.save(permiso);
        return convertirADTO(permisoActualizado);
    }

    public void eliminarPermiso(Long idPermiso) {
        if (!permisoRepository.existsById(idPermiso)) {
            throw new RolNotFoundException("Permiso no encontrado con ID: " + idPermiso);
        }
        permisoRepository.deleteById(idPermiso);
    }

    private PermisoDTO convertirADTO(Permiso permiso) {
        return new PermisoDTO(permiso.getIdPermiso(), permiso.getNombre());
    }
}
