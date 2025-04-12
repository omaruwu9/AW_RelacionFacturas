package com.example.demo.service;

import com.example.demo.entity.Usuario;
import com.example.demo.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String nomina) throws UsernameNotFoundException {
        System.out.println("Autenticando la nómina: " + nomina);
        Usuario user = usuarioRepository.findByNomina(nomina)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        System.out.println("contraseña desde la bd: " + user.getPassword());
        System.out.println("¿Es igual a bcrypt de '1234'? " +
                new BCryptPasswordEncoder().matches("1234", user.getPassword()));
        return User.builder()
                .username(user.getNomina())
                .password(user.getPassword())
                .roles("USER")
                .build();
    }
}
