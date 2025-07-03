package com.example.demo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;


@Controller
public class registrarBitacora {
    @Autowired
    @Qualifier("postgresJdbcTemplate")
    private JdbcTemplate postgresJdbcTemplate;

    @Autowired
    private ObjectMapper objectMapper; // para convertir a JSON

    public void registrarBitacora(HttpSession session, String tabla, String operacion, Object previos, Object nuevos) {
        String nomina = (String) session.getAttribute("usuarioNomina");

        if (nomina == null) {
            System.err.println("⚠ No hay nomina en sesión");
            return;
        }

        try {
            String datosPreviosJson = (previos != null) ? objectMapper.writeValueAsString(previos) : null;
            String datosNuevosJson = (nuevos != null) ? objectMapper.writeValueAsString(nuevos) : null;

            postgresJdbcTemplate.update(
                    "INSERT INTO bitacora (id_usuario, tabla_afectada, operacion, datos_previos, datos_nuevos) " +
                            "VALUES (?, ?, ?, ?::jsonb, ?::jsonb)",
                    nomina, tabla, operacion, datosPreviosJson, datosNuevosJson
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
