package com.bolsadeideas.backend.muebleria.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bolsadeideas.backend.muebleria.dtos.ImagenCatalogoDTO;

import net.coobird.thumbnailator.Thumbnails;



@Service
public class CatalogoImageStorageService {
	
	private final Path originalPath;
	private final Path miniPath;

    public CatalogoImageStorageService( @Value("${app.storage.catalogo}") String rutaCatalogo) {

        Path catalogoPath = Paths.get(rutaCatalogo).toAbsolutePath().normalize();
        //Creo la rutas de mini y fotos normales donde se guardaran
        this.originalPath = catalogoPath.resolve("original").normalize();

        this.miniPath = catalogoPath.resolve("mini").normalize();

        try {
            Files.createDirectories(originalPath);
            Files.createDirectories(miniPath);
        } catch (IOException e) {
            throw new RuntimeException( "No se pudo crear la carpeta del catálogo", e);
        }
    }

    public ImagenCatalogoDTO guardar(MultipartFile archivo) {
        
        validarArchivo(archivo);

        String nombreOriginal = archivo.getOriginalFilename();

        String extension = obtenerExtension(nombreOriginal);

        String nombreArchivo =UUID.randomUUID() + extension;
        
        Path destinoOriginal = originalPath.resolve(nombreArchivo).normalize();

        Path destinoMini = miniPath.resolve(nombreArchivo).normalize();

        validarRuta(destinoOriginal, originalPath);
        validarRuta(destinoMini, miniPath);

        try {
        	//ORIGINAL
        	Files.copy(archivo.getInputStream(), destinoOriginal, StandardCopyOption.REPLACE_EXISTING);
        	
        	//MINIATURA
        	Thumbnails.of(destinoOriginal.toFile())
        		.size(500, 500)
        		.keepAspectRatio(true)
        		.toFile(destinoMini.toFile());


            return ImagenCatalogoDTO.builder()
            		.imagenUrl(nombreArchivo)
            		.miniaturaUrl(nombreArchivo)
            		.build();

        } catch (IOException e) {
            throw new RuntimeException("No se pudo guardar la imagen", e);
        }
    }

    public Resource cargarOriginal(String nombreArchivo) {

       return cargar(originalPath, nombreArchivo);
    }
    
    public Resource cargarMiniatura(String nombreArchivo) {

        return cargar(miniPath, nombreArchivo);
    }
    
    private Resource cargar(Path carpeta, String nombreArchivo) {
    	try {

            Path archivo = carpeta.resolve(nombreArchivo).normalize();

            validarRuta(archivo, carpeta);

            Resource recurso = new UrlResource(archivo.toUri());

            if (!recurso.exists() || !recurso.isReadable()) {
                throw new RuntimeException("Imagen no encontrada");
            }

            return recurso;

        } catch (Exception e) {

            throw new RuntimeException("No se pudo cargar la imagen", e);
        }
    }
    
    private void validarArchivo(MultipartFile archivo) {

        if (archivo == null || archivo.isEmpty()) {
            throw new IllegalArgumentException("La imagen es obligatoria");
        }

        String contentType = archivo.getContentType();

        if (contentType == null || !(contentType.equals("image/jpeg") || contentType.equals("image/png"))) {
            throw new IllegalArgumentException("Formato de imagen no permitido");
        }
    }

    private void validarRuta(Path archivo, Path carpeta) {

        if (!archivo.startsWith(carpeta)) {
            throw new IllegalArgumentException("Ruta de archivo inválida");
        }
    }

    private String obtenerExtension(String nombre) {

        if (nombre == null) {
            return "";
        }

        int punto = nombre.lastIndexOf('.');

        if (punto < 0) {
            return "";
        }

        return nombre.substring(punto).toLowerCase();
    }
	
}
