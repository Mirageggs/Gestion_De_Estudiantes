package com.colegio.gestionacceso.controller;

import com.colegio.gestionacceso.dto.RolDTO;
import com.colegio.gestionacceso.service.RolService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RolController {

    private final RolService rolService;

    public RolController(RolService rolService) {
        this.rolService = rolService;
    }

    @GetMapping
    public List<RolDTO> listar() {
        return rolService.listar();
    }

    @GetMapping("/{id}")
    public RolDTO buscarPorId(@PathVariable Long id) {
        return rolService.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public RolDTO crear(@Valid @RequestBody RolDTO dto) {
        return rolService.crear(dto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public RolDTO actualizar(@PathVariable Long id, @Valid @RequestBody RolDTO dto) {
        return rolService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void eliminar(@PathVariable Long id) {
        rolService.eliminar(id);
    }
}
