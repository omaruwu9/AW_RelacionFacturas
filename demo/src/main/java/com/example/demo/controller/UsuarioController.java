package com.example.demo.controller;

import com.example.demo.entity.Usuario;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/registro")
    public String registrarUsuario(Usuario usuario, RedirectAttributes redirectAttributes) {
        // Validar si ya existe
        if (usuarioRepository.findByNomina(usuario.getNomina()).isPresent()) {
            redirectAttributes.addFlashAttribute("error", "El usuario ya está registrado.");
            return "redirect:/login";
        }

        System.out.println(">>> ENTRANDO A REGISTRO");
        System.out.println("nomina: " + usuario.getNomina());
        System.out.println("email: " + usuario.getEmail());
        System.out.println("password: " + usuario.getPassword());

        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setId_rol(2);
        usuarioRepository.save(usuario);

        redirectAttributes.addFlashAttribute("success", "Usuario registrado correctamente");
        return "redirect:/login";
    }

    @PostMapping("/guardar")
    public String guardarUsuario(@ModelAttribute Usuario usuario) {
        // Buscar el usuario actual por ID si ya existe
        Optional<Usuario> usuarioExistente = usuarioRepository.findById(usuario.getId());

        if (usuarioExistente.isPresent()) {
            Usuario existente = usuarioExistente.get();

            // Si la contraseña cambió, la encriptamos
            if (!usuario.getPassword().equals(existente.getPassword())) {
                usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            } else {
                // Si es la misma (ya cifrada), no la volvemos a cifrar
                usuario.setPassword(existente.getPassword());
            }
        } else {
            // Es un usuario nuevo, ciframos la contraseña
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }

        usuarioRepository.save(usuario);
        return "redirect:/usuarios";
    }

}
