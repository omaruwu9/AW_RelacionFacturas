package com.example.demo.controller;

import com.example.demo.entity.Usuario;
import com.example.demo.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
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


}
