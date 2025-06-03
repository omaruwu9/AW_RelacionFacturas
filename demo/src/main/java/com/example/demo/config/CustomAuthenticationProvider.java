package com.example.demo.config;

import com.example.demo.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder; // ← necesario

    @Override
    public Authentication authenticate(Authentication authentication) {
        String nomina = authentication.getName();
        String rawPassword = authentication.getCredentials().toString();

        return usuarioRepository.findByNomina(nomina)
                .filter(usuario -> passwordEncoder.matches(rawPassword, usuario.getPassword())) // ← validación aquí
                .map(usuario -> {
                    UserDetails userDetails = User
                            .withUsername(usuario.getNomina())
                            .password(usuario.getPassword()) // puede usarse aquí
                            .authorities(Collections.emptyList())
                            .build();

                    return new UsernamePasswordAuthenticationToken(
                            userDetails,
                            rawPassword,
                            userDetails.getAuthorities()
                    );
                })
                .orElseThrow(() -> new RuntimeException("Nomina o contraseña incorrecta"));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}