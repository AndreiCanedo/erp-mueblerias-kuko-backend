package com.bolsadeideas.backend.muebleria.control;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bolsadeideas.backend.muebleria.pdf.OrdenCompraPdf.OrdenCompraPdfService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/ordenes")
@RequiredArgsConstructor
public class OrdenCompraPdfController {
	
	 private final OrdenCompraPdfService ordenCompraPdfService;


	 @GetMapping("/{ordenId}/pdf")
	 public ResponseEntity<byte[]> descargarPdf(@PathVariable Long ordenId) {

		 byte[] pdf = ordenCompraPdfService.generarPdf(ordenId);

		 String nombreArchivo = "orden-compra-" + ordenId + ".pdf";

		 return ResponseEntity.ok()
			.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nombreArchivo + "\"")
			.contentType(MediaType.APPLICATION_PDF)
			.contentLength(pdf.length)
			.body(pdf);
	 }
	
}
