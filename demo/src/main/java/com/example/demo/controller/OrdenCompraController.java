package com.example.demo.controller;

import com.example.demo.entity.Llenado;
import com.example.demo.entity.LlenadoId;
import com.example.demo.entity.OrdenCompra;
import com.example.demo.entity.Usuario;
import com.example.demo.repository.LlenadoRepository;
import com.example.demo.repository.OrdenCompraRepository;
import com.example.demo.repository.UsuarioRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import jcifs.CIFSContext;
import jcifs.context.SingletonContext;
import jcifs.smb.NtlmPasswordAuthenticator;
import jcifs.smb.SmbFile;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/ordenes-compra")
public class OrdenCompraController {

    @Autowired
    private OrdenCompraRepository ordenCompraRepository;

    @Autowired
    private LlenadoRepository llenadoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private LlenadoController llenadoController;

    Map<String, List<String>> centrosPorRol = Map.of(
            "Usuario_IT", List.of("05-050", "08-085"),
            "Usuario_RH", List.of("02-020", "08-082"),
            "Usuario_OP", List.of("04-040", "08-084"),
            "Usuario_PL", List.of("03-030", "08-083"),
            "Usuario_EHS", List.of("06-060"),
            "Usuario_MTTO", List.of("08-087"),
            "Expatriados", List.of("07-070")
            // etc...
    );


    @GetMapping
    public List<OrdenCompra> getAllOrdenesCompra(Authentication authentication) {
        // Obtener usuario autenticado por su 'nomina'
        String nomina = authentication.getName();
        Usuario usuario = usuarioRepository.findByNomina(nomina).orElse(null);

        if (usuario == null) {
            return List.of(); // o lanzar excepción si quieres forzar autenticación válida
        }

        int idRol = usuario.getId_rol();

        List<String> centrosPermitidos;

        switch (idRol) {
            case 2: // Usuario_IT
                centrosPermitidos = List.of("05-050", "08-085");
                break;
            case 3: // Usuario_RH
                centrosPermitidos = List.of("02-020", "08-082");
                break;
            case 4: // Usuario_OP
                centrosPermitidos = List.of("04-040", "08-084");
                break;
            case 5: // Usuario_PL
                centrosPermitidos = List.of("03-030", "08-083");
                break;
            case 6: // Usuario_EHS
                centrosPermitidos = List.of("06-060");
                break;
            case 7: // Usuario_MTTO
                centrosPermitidos = List.of("08-087");
                break;
            case 8: // Expatriados
                centrosPermitidos = List.of("07-070");
                break;
            case 1: // Administrador
                return ordenCompraRepository.findAll(); // Admin ve todo
            default:
                centrosPermitidos = List.of(); // No tiene acceso a nada
        }

        return ordenCompraRepository.findByCentroCostoIn(centrosPermitidos);
    }



    //recupera los detalles de la orden de compra, es decir, la información que se encuentra disponible (orden de compra y lo llenado de la
    //factura)
    @GetMapping("/detalle-orden/{id}")
    public ResponseEntity<?> obtenerDetallesOrden(@PathVariable Integer id) {
        Optional<OrdenCompra> orden = ordenCompraRepository.findById(id);

        if (orden.isPresent()) {
            // Crear manualmente el LlenadoId
            LlenadoId llenadoId = new LlenadoId(id);
            Optional<Llenado> llenado = llenadoRepository.findById(llenadoId);

            Map<String, Object> respuesta = new HashMap<>();

            // Datos de orden_compra
            respuesta.put("solicitudOC", orden.get().getSolicitudOc());
            respuesta.put("fechaSolicitud", orden.get().getFechaRequerida());
            respuesta.put("ordenCompra", orden.get().getOrdenCompra());
            respuesta.put("descripcion", orden.get().getDescripcionOC());
            respuesta.put("fechaEmision", orden.get().getFecha_emision());
            respuesta.put("fechaOC", orden.get().getFecha_orden());
            respuesta.put("centroCosto", orden.get().getCentroCosto());
            respuesta.put("cuentaContable", orden.get().getCuentaContable());
            respuesta.put("descCC", orden.get().getDescCuentaContable());
            respuesta.put("nombreProveedor", orden.get().getNombreProveedor());
            respuesta.put("estado", orden.get().getEstado());

            // Solo agregar datos de llenado si los encuentra
            if (llenado.isPresent()) {
                respuesta.put("factura", llenado.get().getFactura());
                respuesta.put("fechaFactura", llenado.get().getFecha());
                respuesta.put("folioFiscal", llenado.get().getFolio_fiscal());
                respuesta.put("cantidad", llenado.get().getCantidad());
                respuesta.put("moneda", llenado.get().getMoneda());
                respuesta.put("PU", llenado.get().getPu());
                respuesta.put("subtotal", llenado.get().getSubtotal());
                respuesta.put("IVAporc", llenado.get().getIva_porc());
                respuesta.put("total", llenado.get().getTotal());
                respuesta.put("tipoCambio", llenado.get().getTipo_cambio());
                respuesta.put("pesosTotales", llenado.get().getPesos_totales());
                respuesta.put("poliza", llenado.get().getPoliza_garantia());
                respuesta.put("familia", llenado.get().getId_familia());
                respuesta.put("responsable", llenado.get().getId_responsable());
                respuesta.put("presupuesto", llenado.get().getId_extpresupuesto());
            }

            return ResponseEntity.ok(respuesta);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron detalles para la orden.");
        }
    }

    @GetMapping("/ver-pdf")
    public void verPdf(@RequestParam("id") Integer idOrden, HttpServletResponse response) throws IOException {
        String nombreArchivo = idOrden.toString() + ".xml";
        // Construir la ruta dinámica del archivo
        String ruta = "PROYECTO_CODIGO/pdf/" + nombreArchivo + ".xml.pdf"; // Asegúrate que esta ruta sea correcta
        File file = new File(ruta);

        if (file.exists()) {
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline; filename=" + nombreArchivo + ".pdf");
            Files.copy(file.toPath(), response.getOutputStream());
            response.getOutputStream().flush();
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "El archivo no existe.");
        }
    }


}

