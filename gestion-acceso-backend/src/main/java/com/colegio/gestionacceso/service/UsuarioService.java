package com.colegio.gestionacceso.service;

import com.colegio.gestionacceso.dto.UsuarioRequestDTO;
import com.colegio.gestionacceso.dto.UsuarioResponseDTO;
import com.colegio.gestionacceso.model.Rol;
import com.colegio.gestionacceso.model.Usuario;
import com.colegio.gestionacceso.repository.RolRepository;
import com.colegio.gestionacceso.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, RolRepository rolRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // CREATE
    @Transactional
    public UsuarioResponseDTO crear(UsuarioRequestDTO request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        Rol rol = rolRepository.findByNombre(request.getRolNombre())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        Usuario usuario = new Usuario();
        usuario.setEmail(request.getEmail());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setNombre(request.getNombre());
        usuario.setRol(rol);
        usuario.setActivo(true);

        Usuario guardado = usuarioRepository.save(usuario);
        return mapearADTO(guardado);
    }

    // READ ALL
    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> obtenerTodos() {
        return usuarioRepository.findAll().stream()
                .map(this::mapearADTO)
                .collect(Collectors.toList());
    }

    // READ ONE
    @Transactional(readOnly = true)
    public UsuarioResponseDTO obtenerPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return mapearADTO(usuario);
    }

    // UPDATE
    @Transactional
    public UsuarioResponseDTO actualizar(Long id, UsuarioRequestDTO request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Rol rol = rolRepository.findByNombre(request.getRolNombre())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        usuario.setEmail(request.getEmail());
        usuario.setNombre(request.getNombre());
        usuario.setRol(rol);
        
        // Solo actualizamos la contraseña si se envía una nueva
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        Usuario actualizado = usuarioRepository.save(usuario);
        return mapearADTO(actualizado);
    }

    // DELETE (Soft Delete)
    @Transactional
    public void eliminar(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        usuario.setActivo(false); // Baja lógica
        usuarioRepository.save(usuario);
    }

    // Método auxiliar de mapeo
    private UsuarioResponseDTO mapearADTO(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getEmail(),
                usuario.getNombre(),
                usuario.getRol().getNombre(),
                usuario.isActivo()
        );
    }
}