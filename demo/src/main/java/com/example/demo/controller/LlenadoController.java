package com.example.demo.controller;

import com.example.demo.entity.FamiliaGrafica;
import com.example.demo.entity.Llenado;
import com.example.demo.entity.OrdenCompra;
import com.example.demo.repository.LlenadoRepository;
import com.example.demo.repository.OrdenCompraRepository;
import com.example.demo.service.LlenadoService;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.itextpdf.text.BaseColor;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Controller
public class LlenadoController {

    @Autowired
    private OrdenCompraRepository ordenCompraRepository;

    private final LlenadoService llenadoService;

    private static final String BASE_DIR = "PROYECTO_CODIGO/";
    private static final String XML_DIR = BASE_DIR + "xml/";
    private static final String PDF_DIR = BASE_DIR + "pdf/";


    //se obtiene las ordenes de compra por medio de su llave primaria (id)
    @GetMapping("/api/ordenes-compra/{id}")
    public ResponseEntity<OrdenCompra> obtenerOrdenPorId(@PathVariable Integer id) {
        return ordenCompraRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //se despliega el ofrmulario para el llenado de información de la orden de comrpa que el usuario seleccione en la aplicación
    @GetMapping("/llenado/{id}")
    public String mostrarFormulario(@PathVariable("id") Integer id, Model model) {
        Optional<OrdenCompra> ordenOpt = ordenCompraRepository.findById(id);
        if (ordenOpt.isPresent()) {
            model.addAttribute("orden", ordenOpt.get());
            return "formulario_llenado"; // nombre del HTML (sin .html)
        } else {
            return "redirect:/error"; // o puedes mostrar una vista de error personalizada
        }
    }

    @GetMapping("/llenadoAdm/{id}")
    public String mostrarFormularioAdm(@PathVariable("id") Integer id, Model model) {
        Optional<OrdenCompra> ordenOpt = ordenCompraRepository.findById(id);
        if (ordenOpt.isPresent()) {
            model.addAttribute("orden", ordenOpt.get());
            return "formulario_llenadoAdm"; // nombre del HTML (sin .html)
        } else {
            return "redirect:/error"; // o puedes mostrar una vista de error personalizada
        }
    }

    @GetMapping("/api/llenado/{id}")
    @ResponseBody
    public ResponseEntity<Llenado> obtenerLlenadoPorId(@PathVariable Integer id) {
        Optional<Llenado> llenadoOpt = llenadoService.obtenerPorId(id);
        return llenadoOpt.map(ResponseEntity::ok).orElse(ResponseEntity.noContent().build());
    }


    @GetMapping("/volver")
    public String volverAOrdenes() {
        return "mod_llenado";
    }

    @GetMapping("/volverAdm")
    public String volverAOrdenesAdm() {
        return "mod_llenadoAdm";
    }

    public LlenadoController(LlenadoService llenadoService) {
        this.llenadoService = llenadoService;
    }

    //guarda los datos que se ingresaron en el formulario del llenado
    @PostMapping("/llenadoG")
    public ResponseEntity<?> guardarLlenadoConArchivo(
            @RequestPart("llenado") Llenado llenado,
            @RequestPart("archivoXml") MultipartFile archivo) {
        try {
            // Crear carpetas si no existen
            Files.createDirectories(Paths.get(XML_DIR));
            Files.createDirectories(Paths.get(PDF_DIR));

            String nombreArchivo = StringUtils.cleanPath(archivo.getOriginalFilename());
            String extension = nombreArchivo.substring(nombreArchivo.lastIndexOf('.') + 1);
            String nombreBase = llenado.getId() + "." + extension;

            String xmlPath = null;
            String pdfPath = null;

            if ("xml".equalsIgnoreCase(extension)) {
                xmlPath = XML_DIR + "/" + nombreBase;
                Files.copy(archivo.getInputStream(), Paths.get(xmlPath), StandardCopyOption.REPLACE_EXISTING);
                pdfPath = PDF_DIR + "/" + nombreBase + ".pdf";
                generarPdfDesdeXml(xmlPath, pdfPath);
            }

            llenado.setRutaXml(xmlPath);
            llenado.setRutaPdf(pdfPath);

            Llenado guardado = llenadoService.guardar(llenado);
            return ResponseEntity.ok(guardado);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar el llenado y archivos");
        }
    }

    private void generarPdfDesdeXml(String xmlPath, String pdfPath) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        org.w3c.dom.Document xmlDoc = builder.parse(new File(xmlPath));
        xmlDoc.getDocumentElement().normalize();

        Document pdfDoc = new Document(PageSize.A4, 40, 40, 50, 50);
        PdfWriter.getInstance(pdfDoc, new FileOutputStream(pdfPath));
        pdfDoc.open();

        // Logo
        try {
            Image logo = Image.getInstance("demo/src/main/resources/static/images/promex-logo-removebg-preview.png");
            logo.scaleAbsolute(150, 50);
            logo.setAlignment(Image.ALIGN_CENTER);
            pdfDoc.add(logo);
        } catch (Exception e) {
            System.out.println("Logo no encontrado o error al cargar: " + e.getMessage());
        }

        pdfDoc.add(Chunk.NEWLINE);
        Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 9);

        pdfDoc.add(Chunk.NEWLINE);

        Paragraph paragraphInfo = new Paragraph(" Este documento no cuenta como un comprobante fiscal, su uso es meramente informativo. ", boldFont);
        paragraphInfo.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
        pdfDoc.add(paragraphInfo);

        pdfDoc.add(Chunk.NEWLINE);

//---------------------------------------------------------------------------------------------------------
        // Datos generales del CFDI
        Element comprobante = xmlDoc.getDocumentElement();
        Element TimbreFiscalDigital = xmlDoc.getDocumentElement();
        String fecha = comprobante.getAttribute("Fecha");
        String folio = comprobante.getAttribute("Folio");


        Map<String, String> metodosPago = new HashMap<>();
        metodosPago.put("PUE", "Pago en una sola exhibición");
        metodosPago.put("PIP", "Pago inicial y en parcialidades");
        metodosPago.put("PPD", "Pago en parcialidades o diferido");
        String metodoPago = comprobante.getAttribute("MetodoPago");
        String descripcionMetodoPago = metodosPago.getOrDefault(metodoPago, "Desconocida (" + metodoPago + ")");


        Map<String, String> monedas = new HashMap<>();
        monedas.put("MXN", "Peso Mexicano");
        monedas.put("USD", "Dolares");
        monedas.put("EUR", "Euros");
        String moneda = comprobante.getAttribute("Moneda");
        String descripcionMonedas = metodosPago.getOrDefault(moneda, "Desconocida (" + moneda + ")");

        Map<String, String> formasPago = new HashMap<>();
        formasPago.put("01", "Efectivo");
        formasPago.put("02", "Cheque Nominativo");
        formasPago.put("03", "Transferencia Electrónica de Fondos SPEI");
        formasPago.put("04", "Tarjeta de Crédito");
        formasPago.put("05", "Monedero Electrónico");
        formasPago.put("06", "Dinero Electrónico");
        formasPago.put("08", "Vales de Despensa");
        formasPago.put("12", "Dación en Pago");
        formasPago.put("13", "Pago por Subrogación");
        formasPago.put("14", "Pago por Consignación");
        formasPago.put("15", "Condonación");
        formasPago.put("17", "Compensación");
        formasPago.put("23", "Novación");
        formasPago.put("24", "Confusión");
        formasPago.put("25", "Remisión de Deuda");
        formasPago.put("26", "Prescripción o Caducidad");
        formasPago.put("27", "A Satisfacción del Acreedor");
        formasPago.put("28", "Tarjeta de Débito");
        formasPago.put("29", "Tarjeta de Servicios");
        formasPago.put("30", "Aplicación de Anticipos");
        formasPago.put("31", "Intermediario Pagos");
        formasPago.put("99", "Por Definir");
        String formaPago = comprobante.getAttribute("FormaPago");
        String descripcionFormaPago = formasPago.getOrDefault(formaPago, "Desconocida (" + formaPago + ")");

        String CondicionesDePago = comprobante.getAttribute("CondicionesDePago");


        // Buscar el UUID dentro del nodo TimbreFiscalDigital en cfdi:Complemento
        String folioFiscal = "";
        String fechaTimbrado = "";
        String certificadoSAT = "";
        NodeList complementoList = xmlDoc.getElementsByTagName("cfdi:Complemento");
        for (int i = 0; i < complementoList.getLength(); i++) {
            Node complemento = complementoList.item(i);
            if (complemento.getNodeType() == Node.ELEMENT_NODE) {
                NodeList hijos = complemento.getChildNodes();
                for (int j = 0; j < hijos.getLength(); j++) {
                    Node hijo = hijos.item(j);
                    if (hijo.getNodeType() == Node.ELEMENT_NODE && hijo.getNodeName().endsWith("TimbreFiscalDigital")) {
                        Element timbre = (Element) hijo;
                        folioFiscal = timbre.getAttribute("UUID");
                        fechaTimbrado = timbre.getAttribute("FechaTimbrado");
                        certificadoSAT = timbre.getAttribute("NoCertificadoSAT");
                        break;
                    }
                }
            }
        }

        // Crear la tabla con 2 columnas
        PdfPTable tabla = new PdfPTable(2);
        tabla.setWidthPercentage(100);
        tabla.setWidths(new float[]{1, 1});
        tabla.setSpacingBefore(10f);
        tabla.setSpacingAfter(10f);

        // Crear la celda de la izquierda (Datos del Timbrado)
        PdfPCell celdaTimbrado = new PdfPCell();
        celdaTimbrado.setBorder(Rectangle.NO_BORDER);
        celdaTimbrado.setBorderWidthRight(0.5f); // solo la división vertical
        celdaTimbrado.setPadding(8f);

        celdaTimbrado.addElement(new Paragraph("DATOS DEL TIMBRADO", boldFont));
        celdaTimbrado.addElement(new Paragraph("Folio Fiscal: " + folioFiscal, normalFont));
        celdaTimbrado.addElement(new Paragraph("Fecha Timbrado: " + fechaTimbrado, normalFont));
        celdaTimbrado.addElement(new Paragraph("Certificado SAT: " + certificadoSAT, normalFont));

        // Crear la celda de la derecha (Datos del Documento)
        PdfPCell celdaDocumento = new PdfPCell();
        celdaDocumento.setBorder(Rectangle.NO_BORDER);
        celdaDocumento.setBorderWidthLeft(0.5f); // solo la división vertical
        celdaDocumento.setPadding(8f);

        celdaDocumento.addElement(new Paragraph("DATOS DEL DOCUMENTO", boldFont));
        celdaDocumento.addElement(new Paragraph("Folio: " + folio, normalFont));
        celdaDocumento.addElement(new Paragraph("Fecha: " + fecha, normalFont));
        celdaDocumento.addElement(new Paragraph("Moneda: " + moneda + " - " + descripcionMonedas, normalFont));
        celdaDocumento.addElement(new Paragraph("Forma de Pago: " + formaPago + " - " + descripcionFormaPago, normalFont));
        celdaDocumento.addElement(new Paragraph("Método de Pago: " + metodoPago + " - " + descripcionMetodoPago, normalFont));
        celdaDocumento.addElement(new Paragraph("Condición de Pago: " + CondicionesDePago, normalFont));

        // Agregar celdas a la tabla
        tabla.addCell(celdaTimbrado);
        tabla.addCell(celdaDocumento);

        // Agregar la tabla al documento PDF
        pdfDoc.add(tabla);
        pdfDoc.add(Chunk.NEWLINE);

//---------------------------------------------------------------------------------------------------------
        // Tabla de Emisor y Receptor lado a lado
        // Obtener nodos Emisor y Receptor del XML
        // Color para encabezados
        BaseColor headerColorER = BaseColor.LIGHT_GRAY;

        // Obtener nodos del XML
        Element emisorElement = (Element) xmlDoc.getElementsByTagName("cfdi:Emisor").item(0);
        Element receptorElement = (Element) xmlDoc.getElementsByTagName("cfdi:Receptor").item(0);

        Map<String, String> regimenFiscales = new HashMap<>();
        regimenFiscales.put("605", "Sueldos y Salarios e Ingresos Asimilados a Salarios");
        regimenFiscales.put("606", "Arrendamiento");
        regimenFiscales.put("608", "Demás ingresos");
        regimenFiscales.put("611", "Ingresos por Dividendos (socios y accionistas)");
        regimenFiscales.put("612", "Personas Físicas con Actividades Empresariales y Profesionales");
        regimenFiscales.put("614", "Ingresos por intereses");
        regimenFiscales.put("615", "Régimen de los ingresos por obtención de premios");
        regimenFiscales.put("616", "Sin obligaciones fiscales");
        regimenFiscales.put("621", "Incorporación Fiscal");
        regimenFiscales.put("622", "Actividades Agrícolas, Ganaderas, Silvícolas y Pesqueras");
        regimenFiscales.put("626", "Régimen Simplificado de Confianza (RESICO)");
        regimenFiscales.put("629", "De los Regímenes Fiscales Preferentes y de las Empresas Multinacionales");
        regimenFiscales.put("630", "Enajenación de acciones en bolsa de valores");
        regimenFiscales.put("601", "General de Ley Personas Morales");
        regimenFiscales.put("603", "Personas Morales con Fines no Lucrativos");
        regimenFiscales.put("607", "Régimen de Enajenación o Adquisición de Bienes");
        regimenFiscales.put("609", "Consolidación");
        regimenFiscales.put("620", "Sociedades Cooperativas de Producción que optan por Diferir sus Ingresos");
        regimenFiscales.put("623", "Opcional para Grupos de Sociedades");
        regimenFiscales.put("624", "Coordinados");
        regimenFiscales.put("628", "Hidrocarburos");
        String RegimenFiscalReceptor = receptorElement.getAttribute("RegimenFiscalReceptor");
        String descripcionRegimenFiscalReceptor = regimenFiscales.getOrDefault(RegimenFiscalReceptor, "Desconocida (" + RegimenFiscalReceptor + ")");
        String RegimenFiscalEmisor = emisorElement.getAttribute("RegimenFiscal");
        String descripcionRegimenFiscalEmisor = regimenFiscales.getOrDefault(RegimenFiscalEmisor, "Desconocida (" + RegimenFiscalEmisor + ")");


        // Tabla principal con 2 columnas (Emisor y Receptor)
        PdfPTable datosTable = new PdfPTable(2);
        datosTable.setWidthPercentage(100);
        datosTable.setWidths(new int[]{1, 1});

        // ===================== EMISOR =====================
        PdfPTable emisorTable = new PdfPTable(1);

        // Celda con título "EMISOR" con fondo gris claro
        PdfPCell emisorHeader = new PdfPCell(new Phrase("EMISOR", boldFont));
        emisorHeader.setBackgroundColor(headerColorER);
        emisorHeader.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
        emisorHeader.setPadding(6f);
        emisorHeader.setBorder(Rectangle.NO_BORDER);
        emisorTable.addCell(emisorHeader);

        // Datos del emisor
        emisorTable.addCell(new Phrase("Nombre: " + emisorElement.getAttribute("Nombre"), normalFont));
        emisorTable.addCell(new Phrase("RFC: " + emisorElement.getAttribute("Rfc"), normalFont));
        emisorTable.addCell(new Phrase("Regimen Fiscal: " + RegimenFiscalEmisor + " - " + descripcionRegimenFiscalEmisor, normalFont));

        PdfPCell emisorCell = new PdfPCell(emisorTable);
        emisorCell.setBorder(Rectangle.NO_BORDER); // solo la tabla externa no tiene borde
        datosTable.addCell(emisorCell);

        // ===================== RECEPTOR =====================
        PdfPTable receptorTable = new PdfPTable(1);

        // Celda con título "RECEPTOR" con fondo gris claro
        PdfPCell receptorHeader = new PdfPCell(new Phrase("RECEPTOR", boldFont));
        receptorHeader.setBackgroundColor(headerColorER);
        receptorHeader.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
        receptorHeader.setPadding(6f);
        receptorHeader.setBorder(Rectangle.NO_BORDER);
        receptorTable.addCell(receptorHeader);

        // Datos del receptor
        receptorTable.addCell(new Phrase("Nombre: " + receptorElement.getAttribute("Nombre"), normalFont));
        receptorTable.addCell(new Phrase("RFC: " + receptorElement.getAttribute("Rfc"), normalFont));

        Map<String, String> usoCFDIs = new HashMap<>();
        usoCFDIs.put("G01", "Adquisión de Mercancias");
        usoCFDIs.put("G02", "Devoluciones, descuentos o bonificaciones");
        usoCFDIs.put("G03", "Gastos en general");
        usoCFDIs.put("I01", "Construcciones");
        usoCFDIs.put("I02", "Mobilario y equipo de oficina por inversiones");
        usoCFDIs.put("I03", "Equipo de transporte");
        usoCFDIs.put("I04", "Equipo de computo y accesorios");
        usoCFDIs.put("I05", "Dados, troqueles, moldes, matrices y herramental");
        usoCFDIs.put("I06", "Comunicaciones telefónicas");
        usoCFDIs.put("I07", "Comunicaciones satelitales");
        usoCFDIs.put("I08", "Otra maquinaria y equipo");
        usoCFDIs.put("D01", "Honorarios médicos, dentales y gastos hospitalarios.");
        usoCFDIs.put("D02", "Gastos médicos por incapacidad o discapacidad");
        usoCFDIs.put("D03", "Gastos funerales.");
        usoCFDIs.put("D04", "Donativos.");
        usoCFDIs.put("D05", "Intereses reales efectivamente pagados por créditos hipotecarios (casa habitación).");
        usoCFDIs.put("D06", "Aportaciones voluntarias al SAR.");
        usoCFDIs.put("D07", "Primas por seguros de gastos médicos.");
        usoCFDIs.put("D08", "Gastos de transportación escolar obligatoria.");
        usoCFDIs.put("D09", "Depósitos en cuentas para el ahorro, primas que tengan como base planes de pensiones.");
        usoCFDIs.put("D10", "Pagos por servicios educativos (colegiaturas).");
        usoCFDIs.put("EP01", "Por definir");
        String UsoCFDI = receptorElement.getAttribute("UsoCFDI");
        String descripcionUsoCFDI = usoCFDIs.getOrDefault(UsoCFDI, "Desconocida (" + UsoCFDI + ")");
        receptorTable.addCell(new Phrase("Uso CFDI: " + UsoCFDI + " - " + descripcionUsoCFDI, normalFont));

        receptorTable.addCell(new Phrase("Domicilio Fiscal: " + receptorElement.getAttribute("DomicilioFiscalReceptor"), normalFont));
        receptorTable.addCell(new Phrase("Regimen Fiscal: " + RegimenFiscalReceptor + " - " + descripcionRegimenFiscalReceptor, normalFont));

        PdfPCell receptorCell = new PdfPCell(receptorTable);
        receptorCell.setBorder(Rectangle.NO_BORDER); // la tabla externa no tiene borde
        datosTable.addCell(receptorCell);

        // Agregar tabla completa al documento
        pdfDoc.add(datosTable);
        pdfDoc.add(Chunk.NEWLINE);

//---------------------------------------------------------------------------------------------------------
        //CONCEPTOS
        Paragraph Conceptos = new Paragraph("CONCEPTOS", boldFont);
        Conceptos.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
        Conceptos.setSpacingBefore(10f);
        Conceptos.setSpacingAfter(5f);
        pdfDoc.add(Conceptos);

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setSpacingBefore(4f);
        table.setSpacingAfter(4f);
        table.setWidths(new int[]{1, 5, 2, 2});

        BaseColor headerColor = BaseColor.LIGHT_GRAY;

        // Encabezados con fondo gris
        Stream.of("Cantidad", "Descripción", "P. Unitario", "Importe").forEach(titulo -> {
            PdfPCell header = new PdfPCell(new Phrase(titulo, boldFont));
            header.setBackgroundColor(headerColor);
            header.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            header.setVerticalAlignment(com.itextpdf.text.Element.ALIGN_MIDDLE);
            header.setPadding(6f);
            header.setBorderWidth(1f);
            table.addCell(header);
        });

        // Celdas de contenido
        NodeList conceptos = xmlDoc.getElementsByTagName("cfdi:Concepto");
        for (int i = 0; i < conceptos.getLength(); i++) {
            Node nodo = conceptos.item(i);
            if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) nodo;

                PdfPCell cantidadCell = new PdfPCell(new Phrase(elem.getAttribute("Cantidad"), normalFont));
                PdfPCell descCell = new PdfPCell(new Phrase(elem.getAttribute("Descripcion"), normalFont));
                PdfPCell valorCell = new PdfPCell(new Phrase(elem.getAttribute("ValorUnitario"), normalFont));
                PdfPCell importeCell = new PdfPCell(new Phrase(elem.getAttribute("Importe"), normalFont));

                Stream.of(cantidadCell, descCell, valorCell, importeCell).forEach(cell -> {
                    cell.setPadding(5f);
                    cell.setBorderWidth(1f);
                });

                table.addCell(cantidadCell);
                table.addCell(descCell);
                table.addCell(valorCell);
                table.addCell(importeCell);
            }
        }

        pdfDoc.add(table);
        pdfDoc.add(Chunk.NEWLINE);


        // ===================== IMPUESTOS =====================
        Paragraph Impuestos = new Paragraph("IMPUESTOS", boldFont);
        Impuestos.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
        Impuestos.setSpacingBefore(10f);
        Impuestos.setSpacingAfter(5f);
        pdfDoc.add(Impuestos);

        PdfPTable impuestosTable = new PdfPTable(4);
        impuestosTable.setWidthPercentage(100);
        impuestosTable.setSpacingBefore(4f);
        impuestosTable.setSpacingAfter(4f);
        impuestosTable.setWidths(new int[]{2, 3, 3, 2});

        Stream.of("Tipo", "Impuesto", "Tasa/Cuota", "Importe").forEach(col -> {
            PdfPCell cell = new PdfPCell(new Phrase(col, boldFont));
            cell.setBackgroundColor(headerColor);
            cell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            cell.setVerticalAlignment(com.itextpdf.text.Element.ALIGN_MIDDLE);
            cell.setPadding(6f);
            cell.setBorderWidth(1f);
            impuestosTable.addCell(cell);
        });

        // Obtener nodos de impuestos
        NodeList impuestosList = xmlDoc.getElementsByTagName("cfdi:Impuestos");
        if (impuestosList.getLength() > 1) {
            Node impuestosNode = impuestosList.item(1);
            if (impuestosNode.getNodeType() == Node.ELEMENT_NODE) {
                Element impuestosElement = (Element) impuestosNode;

                NodeList traslados = impuestosElement.getElementsByTagName("cfdi:Traslado");
                for (int j = 0; j < traslados.getLength(); j++) {
                    Element traslado = (Element) traslados.item(j);
                    impuestosTable.addCell(new PdfPCell(new Phrase("Traslado", normalFont)));
                    impuestosTable.addCell(new PdfPCell(new Phrase(traslado.getAttribute("Impuesto"), normalFont)));
                    impuestosTable.addCell(new PdfPCell(new Phrase(traslado.getAttribute("TasaOCuota"), normalFont)));
                    impuestosTable.addCell(new PdfPCell(new Phrase("$" + traslado.getAttribute("Importe"), normalFont)));
                }

                NodeList retenciones = impuestosElement.getElementsByTagName("cfdi:Retencion");
                for (int j = 0; j < retenciones.getLength(); j++) {
                    Element retencion = (Element) retenciones.item(j);
                    impuestosTable.addCell(new PdfPCell(new Phrase("Retención", normalFont)));
                    impuestosTable.addCell(new PdfPCell(new Phrase(retencion.getAttribute("Impuesto"), normalFont)));
                    impuestosTable.addCell(new PdfPCell(new Phrase(retencion.getAttribute("TasaOCuota"), normalFont)));
                    impuestosTable.addCell(new PdfPCell(new Phrase("$" + retencion.getAttribute("Importe"), normalFont)));
                }
            }
        }

        // Ajustar padding y bordes a todas las celdas de contenido
        for (PdfPCell cell : impuestosTable.getRows().stream()
                .flatMap(row -> row.getCells() != null ? Stream.of(row.getCells()) : Stream.empty())
                .toList()) {
            if (cell != null) {
                cell.setPadding(5f);
                cell.setBorderWidth(1f);
            }
        }

        pdfDoc.add(impuestosTable);
        pdfDoc.add(Chunk.NEWLINE);

//---------------------------------------------------------------------------------------------------------
        // Totales
        String subTotal = comprobante.getAttribute("SubTotal");
        String total = comprobante.getAttribute("Total");

        Paragraph subTotal2 = new Paragraph(" Subtotal: $" + subTotal, boldFont);
        subTotal2.setAlignment(com.itextpdf.text.Element.ALIGN_RIGHT);
        pdfDoc.add(subTotal2);
        Paragraph Total2 = new Paragraph(" Total: $" + total, boldFont);
        Total2.setAlignment(com.itextpdf.text.Element.ALIGN_RIGHT);
        pdfDoc.add(Total2);



        pdfDoc.close();
    }


}
