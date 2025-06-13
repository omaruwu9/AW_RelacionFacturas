package com.example.demo.config;

import com.example.demo.entity.Usuario;
import com.example.demo.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UsuarioRepository usuarioRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        String nomina = authentication.getName();
        Usuario usuario = usuarioRepository.findByNomina(nomina)
                .orElseThrow();

        if (usuario.getId_rol() == 9) {
            response.sendRedirect("/login");
            return; // Importante para evitar que siga ejecutando el resto
        }

        if (usuario.getId_rol() == 1) {
            response.sendRedirect("/adm_panel"); //si el rol de usuario es 1 (Administrador) se redirige al panel de administrador
        } else {
            response.sendRedirect("/dashboard"); //si el rol es otro, por el momento se estará redirigiendo al panel de usuarios
        }
    }
}

