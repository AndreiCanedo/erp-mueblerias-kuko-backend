package com.bolsadeideas.backend.muebleria.dtos;

import java.time.LocalDateTime;

import com.bolsadeideas.backend.muebleria.model.CategoriaMueble;

import lombok.Data;

@Data
public class DisenoMuebleDTO {
	
	 private Long id;
	 private String nombre;
	 private String descripcion;
	 private CategoriaMueble categoria;
	 private String imagenUrl;
	 private String miniaturaUrl;
	 private LocalDateTime fechaRegistro;
	 private Boolean activo;
}
