package com.example.demo.config;

import com.example.demo.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UsuarioRepository usuarioRepository;

    //el siguiente metodo es pra validar con respecto a si el uusario existe, si detecta que dicho usuario no existe lo que realiza es
    //ignorar el inicio de sesión
    @Override
    public Authentication authenticate(Authentication authentication) {
        String nomina = authentication.getName();

        return usuarioRepository.findByNomina(nomina)
                .map(usuario -> {
                    UserDetails userDetails = User
                            .withUsername(usuario.getNomina())
                            .password("[ignored]")
                            .authorities(Collections.emptyList())
                            .build();

                    return new UsernamePasswordAuthenticationToken(
                            userDetails,
                            authentication.getCredentials(),
                            userDetails.getAuthorities()
                    );
                })
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
