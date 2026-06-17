package com.colegio.gestionacceso.config;

import com.colegio.gestionacceso.model.Rol;
import com.colegio.gestionacceso.model.Usuario;
import com.colegio.gestionacceso.repository.RolRepository;
import com.colegio.gestionacceso.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UsuarioRepository usuarioRepository, RolRepository rolRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        Rol rolAdmin = rolRepository.findByNombre("ADMIN").orElseGet(() -> {
            Rol r = rolRepository.save(new Rol("ADMIN", "Acceso total al sistema"));
            log.info("Rol ADMIN creado");
            return r;
        });

        Rol rolPersonal = rolRepository.findByNombre("PERSONAL").orElseGet(() -> {
            Rol r = rolRepository.save(new Rol("PERSONAL", "Registro de accesos en portería"));
            log.info("Rol PERSONAL creado");
            return r;
        });

        if (!usuarioRepository.existsByEmail("admin@colegio.edu")) {
            Usuario admin = new Usuario();
            admin.setEmail("admin@colegio.edu");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setNombre("Administrador");
            admin.setRol(rolAdmin);
            usuarioRepository.save(admin);
            log.info("Usuario admin creado: admin@colegio.edu / admin123");
        }

        if (!usuarioRepository.existsByEmail("personal@colegio.edu")) {
            Usuario personal = new Usuario();
            personal.setEmail("personal@colegio.edu");
            personal.setPassword(passwordEncoder.encode("personal123"));
            personal.setNombre("Personal Portería");
            personal.setRol(rolPersonal);
            usuarioRepository.save(personal);
            log.info("Usuario personal creado: personal@colegio.edu / personal123");
        }
    }
}
