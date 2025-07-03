package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {
    @GetMapping("/mod_llenado")
    public String mod_llenado() {return "mod_llenado";}

    @GetMapping("/mod_llenadoAdm")
    public String mod_llenadoAdm() {return "mod_llenadoAdm";}

    @GetMapping("/mod_rep")
    public String mod_rep() {return "mod_rep";}

    @GetMapping("/mod_repAdm")
    public String mod_repAdm() {return "mod_repAdm";}

    @GetMapping("/mod_mantenimietno")
    public String mod_Mantenimiento() {return "mod_mantenimiento";}
}
