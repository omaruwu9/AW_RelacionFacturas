package com.example.demo.controller;

import com.example.demo.entity.Usuario;
import com.example.demo.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> request) {
        String nomina = request.get("nomina");
        String password = request.get("password");

        Usuario user = usuarioRepository.findByNomina(nomina).orElse(null);

        if (user == null) {
            return ResponseEntity
                    .status(401)
                    .body(Map.of("error", "Nómina no registrada"));
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            return ResponseEntity
                    .status(401)
                    .body(Map.of("error", "Contraseña incorrecta"));
        }

        // Determinar redirección según el rol
        String redireccion = (user.getId_rol() == 1) ? "/adm_panel" : "/dashboard";

        return ResponseEntity.ok(Map.of(
                "message", "Login exitoso",
                "redirect", redireccion
        ));
    }
}
