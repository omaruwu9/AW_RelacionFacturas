package com.example.demo.controller;

import com.example.demo.entity.Usuario;
import com.example.demo.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> request,
                                                     HttpSession session) {
        String nomina = request.get("nomina");
        String password = request.get("password");

        Usuario user = usuarioRepository.findByNomina(nomina).orElse(null);

        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            return ResponseEntity.status(401).body(Map.of("error", "Credenciales incorrectas"));
        }

        // ✅ Guardar la nómina en sesión (lo que necesitabas)
        session.setAttribute("usuarioNomina", user.getNomina());

        // ✅ Redirección por rol
        String redireccion = (user.getId_rol() == 1) ? "/adm_panel" : "/dashboard";

        return ResponseEntity.ok(Map.of(
                "message", "Login exitoso",
                "redirect", redireccion
        ));
    }

}
