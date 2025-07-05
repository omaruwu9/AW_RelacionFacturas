package com.example.demo.controller;

import com.example.demo.entity.Llenado;
import com.example.demo.entity.LlenadoId;
import com.example.demo.entity.OrdenCompra;
import com.example.demo.entity.Usuario;
import com.example.demo.repository.LlenadoRepository;
import com.example.demo.repository.OrdenCompraRepository;
import com.example.demo.repository.RolCentroCostoRepository;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
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

    @Autowired
    private RolCentroCostoRepository rolCentroCostoRepository;

    @GetMapping
    public List<OrdenCompra> getAllOrdenesCompra(Authentication authentication) {
        String nomina = authentication.getName();
        Usuario usuario = usuarioRepository.findByNomina(nomina).orElse(null);

        if (usuario == null) {
            return List.of();
        }

        int idRol = usuario.getId_rol();

        // Si es administrador, retorna todo
        if (idRol == 1) {
            return ordenCompraRepository.findAll();
        }

        // Obtener centros permitidos desde la base de datos
        List<String> centrosPermitidos = rolCentroCostoRepository.findCentrosCostoByIdRol(idRol);

        if (centrosPermitidos.isEmpty()) {
            return List.of(); // o lanza excepción si quieres
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



}

