package com.colegio.gestionacceso.service;

import com.colegio.gestionacceso.dto.LoginRequestDTO;
import com.colegio.gestionacceso.dto.LoginResponseDTO;
import com.colegio.gestionacceso.dto.UsuarioRequestDTO;
import com.colegio.gestionacceso.exception.BusinessException;
import com.colegio.gestionacceso.config.JwtService;
import com.colegio.gestionacceso.model.Rol;
import com.colegio.gestionacceso.model.Usuario;
import com.colegio.gestionacceso.repository.RolRepository;
import com.colegio.gestionacceso.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UsuarioRepository usuarioRepository,
            RolRepository rolRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public LoginResponseDTO login(LoginRequestDTO request) {
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException("Credenciales inválidas"));

        if (!usuario.isActivo() || !passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            throw new BusinessException("Credenciales inválidas");
        }

        String token = jwtService.generateToken(usuario.getEmail(), usuario.getRol().getNombre());
        return new LoginResponseDTO(token, usuario.getEmail(), usuario.getNombre(), usuario.getRol().getNombre());
    }

    public void crearUsuario(UsuarioRequestDTO request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("El email ya está registrado");
        }
        Rol rol = rolRepository.findByNombre(request.getRolNombre())
                .orElseThrow(() -> new BusinessException("Rol no encontrado: " + request.getRolNombre()));

        Usuario usuario = new Usuario();
        usuario.setEmail(request.getEmail());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setNombre(request.getNombre());
        usuario.setRol(rol);
        usuarioRepository.save(usuario);
    }
}
