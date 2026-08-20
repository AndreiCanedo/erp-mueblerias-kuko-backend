package com.bolsadeideas.backend.muebleria.dtos;

import java.time.LocalDateTime;

import com.bolsadeideas.backend.muebleria.model.CategoriaMueble;

import lombok.Data;

@Data
public class DisenoMuebleResumenDTO {

    private Long id;
    private String nombre;
    private CategoriaMueble categoria;
    private String miniaturaUrl;
    private LocalDateTime fechaRegistro;
    private Long cantidadMuebles;	
	
}
