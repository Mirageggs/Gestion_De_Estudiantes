package com.colegio.gestionacceso.service;

import com.colegio.gestionacceso.dto.RolDTO;
import com.colegio.gestionacceso.exception.BusinessException;
import com.colegio.gestionacceso.exception.ResourceNotFoundException;
import com.colegio.gestionacceso.model.Rol;
import com.colegio.gestionacceso.repository.RolRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolService {

    private final RolRepository rolRepository;

    public RolService(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    public List<RolDTO> listar() {
        return rolRepository.findAll().stream()
                .map(r -> new RolDTO(r.getId(), r.getNombre(), r.getDescripcion()))
                .toList();
    }

    public RolDTO buscarPorId(Long id) {
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con id: " + id));
        return new RolDTO(rol.getId(), rol.getNombre(), rol.getDescripcion());
    }

    public RolDTO crear(RolDTO dto) {
        if (rolRepository.existsByNombre(dto.getNombre())) {
            throw new BusinessException("Ya existe un rol con el nombre: " + dto.getNombre());
        }
        Rol rol = new Rol(dto.getNombre().toUpperCase(), dto.getDescripcion());
        rol = rolRepository.save(rol);
        return new RolDTO(rol.getId(), rol.getNombre(), rol.getDescripcion());
    }

    public RolDTO actualizar(Long id, RolDTO dto) {
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con id: " + id));
        rol.setNombre(dto.getNombre().toUpperCase());
        rol.setDescripcion(dto.getDescripcion());
        rol = rolRepository.save(rol);
        return new RolDTO(rol.getId(), rol.getNombre(), rol.getDescripcion());
    }

    public void eliminar(Long id) {
        if (!rolRepository.existsById(id)) {
            throw new ResourceNotFoundException("Rol no encontrado con id: " + id);
        }
        rolRepository.deleteById(id);
    }
}
