package com.example.demo.controller;

import com.example.demo.entity.Rol;
import com.example.demo.entity.Usuario;
import com.example.demo.repository.RolRepository;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.service.AdmDatosNullService;
import com.example.demo.service.DatosNullService;
import com.example.demo.service.AdmSincronizacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final AdmSincronizacionService admsincronizacionService;
    private final AdmDatosNullService admdatosNullService;

    // Módulo 1: Ver usuarios
    @GetMapping("/usuarios")
    public String verUsuarios(Model model) {
        List<Usuario> usuarios = new ArrayList<>();
        List<Rol> roles = new ArrayList<>();
        usuarioRepository.findAll().forEach(usuarios::add);
        rolRepository.findAll().forEach(roles::add);
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("roles", roles);
        return "usuarios";
    }

    @GetMapping("/api/usuarios/{id}")
    @ResponseBody
    public Usuario obtenerUsuario(@PathVariable Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }


    // Módulo 3: Ejecutar sincronización
    @PostMapping("/sincronizar")
    public String ejecutarSincronizacion(RedirectAttributes redirectAttributes) {
        admsincronizacionService.sincronizarOrdenes();
        //admdatosNullService.actualizarOrdenesIncompletas();
        redirectAttributes.addFlashAttribute("mensaje", "✅ Sincronización ejecutada exitosamente.");
        return "redirect:/adm_panel";
    }

    // Mostrar formulario de edición
    @GetMapping("/usuarios/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable("id") Long id, Model model) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + id));
        model.addAttribute("usuario", usuario);
        return "editar_usuario";
    }

    // Guardar cambios
    @PostMapping("/usuarios/guardar")
    public String guardarEdicionUsuario(@ModelAttribute("usuario") Usuario usuario, RedirectAttributes redirectAttributes) {
        usuarioRepository.save(usuario);
        redirectAttributes.addFlashAttribute("mensaje", "✅ Usuario modificado con éxito.");
        return "redirect:/usuarios";
    }

    // Eliminar usuario
    @PostMapping("/usuarios/eliminar/{id}")
    public String eliminarUsuario(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        usuarioRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("mensaje", "🗑️ Usuario eliminado correctamente.");
        return "redirect:/usuarios";
    }
}
