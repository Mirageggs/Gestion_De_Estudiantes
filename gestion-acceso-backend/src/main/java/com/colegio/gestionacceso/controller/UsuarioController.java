package com.colegio.gestionacceso.controller;

import com.colegio.gestionacceso.dto.UsuarioRequestDTO;
import com.colegio.gestionacceso.dto.UsuarioResponseDTO;
import com.colegio.gestionacceso.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@PreAuthorize("hasRole('ADMIN')") // Protege todos los endpoints del CRUD
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // POST /api/usuarios
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponseDTO crear(@Valid @RequestBody UsuarioRequestDTO request) {
        return usuarioService.crear(request);
    }

    // GET /api/usuarios
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioService.obtenerTodos();
    }

    // GET /api/usuarios/{id}
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UsuarioResponseDTO obtenerPorId(@PathVariable Long id) {
        return usuarioService.obtenerPorId(id);
    }

    // PUT /api/usuarios/{id}
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UsuarioResponseDTO actualizar(@PathVariable Long id, @Valid @RequestBody UsuarioRequestDTO request) {
        return usuarioService.actualizar(id, request);
    }

    // DELETE /api/usuarios/{id}
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        usuarioService.eliminar(id);
    }
}