package com.example.demo.controller;

import com.example.demo.entity.Llenado;
import com.example.demo.entity.LlenadoId;
import com.example.demo.entity.OrdenCompra;
import com.example.demo.repository.LlenadoRepository;
import com.example.demo.repository.OrdenCompraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping
    public List<OrdenCompra> getAllOrdenesCompra() {
        return ordenCompraRepository.findAll();
    }

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

            // Solo agregas datos de llenado si los encuentra
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

