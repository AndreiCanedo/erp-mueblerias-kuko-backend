package com.bolsadeideas.backend.muebleria.control;

import java.io.IOException;
import java.nio.file.Files;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bolsadeideas.backend.muebleria.dtos.ImagenCatalogoDTO;
import com.bolsadeideas.backend.muebleria.services.CatalogoImageStorageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/imagenes/catalogo")
@RequiredArgsConstructor
public class CatalogoImageController {
	
	private final CatalogoImageStorageService storageService;

    @PostMapping
    public ResponseEntity<ImagenCatalogoDTO> subir(@RequestParam("archivo") MultipartFile archivo) {


        return ResponseEntity.ok(storageService.guardar(archivo));
    }

    @GetMapping("/original/{nombreArchivo:.+}")
    public ResponseEntity<Resource> obtenerOriginal(@PathVariable String nombreArchivo) throws IOException {

        Resource recurso = storageService.cargarOriginal(nombreArchivo);

        return construirRespuesta(recurso);
    }
    
    @GetMapping("/mini/{nombreArchivo:.+}")
    public ResponseEntity<Resource> obtenerMiniatura(@PathVariable String nombreArchivo) throws IOException {

        Resource recurso = storageService.cargarOriginal(nombreArchivo);

        return construirRespuesta(recurso);
    }
    
    private ResponseEntity<Resource> construirRespuesta(Resource recurso) throws IOException {

        String contentType = Files.probeContentType( recurso.getFile().toPath());

        return ResponseEntity.ok()
            .contentType(contentType != null
                    ? MediaType.parseMediaType(contentType)
                    : MediaType.APPLICATION_OCTET_STREAM
            )
            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\""
                    + recurso.getFilename()
                    + "\""
            )
            .body(recurso);
    }
	
}
