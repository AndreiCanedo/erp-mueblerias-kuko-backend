package com.bolsadeideas.backend.muebleria.control;


import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bolsadeideas.backend.muebleria.pdf.presupuestoPdf.PresupuestoPdfService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/presupuestos")
@RequiredArgsConstructor
public class PresupuestoPdfController {

	private final PresupuestoPdfService presupuestoPdfService;

    @GetMapping("/{ordenId}/pdf")
    public ResponseEntity<byte[]> descargarPdf(@PathVariable Long ordenId) {

        byte[] pdf = presupuestoPdfService.generarPdf(ordenId);

        String nombreArchivo = "presupuesto-" + ordenId + ".pdf";

        return ResponseEntity.ok()
        	//Esto le dice al navegador que este archivo esta pensado para descargarse con su respectivo nombre
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nombreArchivo + "\"")
            //le informa al cliente que los byts que enviamos es un archivo pdf
            .contentType(MediaType.APPLICATION_PDF)
            //indica el tamaño del archivo, no indispensable pero es buena practica incluirlo
            .contentLength(pdf.length)
            .body(pdf);
    }
}
