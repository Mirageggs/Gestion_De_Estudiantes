package com.colegio.gestionacceso.dto;

public class UsuarioResponseDTO {
    private Long id;
    private String email;
    private String nombre;
    private String rolNombre;
    private boolean activo;

    // Constructor que mapea desde la Entidad
    public UsuarioResponseDTO(Long id, String email, String nombre, String rolNombre, boolean activo) {
        this.id = id;
        this.email = email;
        this.nombre = nombre;
        this.rolNombre = rolNombre;
        this.activo = activo;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getRolNombre() { return rolNombre; }
    public void setRolNombre(String rolNombre) { this.rolNombre = rolNombre; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}