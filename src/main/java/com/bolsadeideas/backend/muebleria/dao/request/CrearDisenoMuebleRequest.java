package com.bolsadeideas.backend.muebleria.dao.request;

import com.bolsadeideas.backend.muebleria.model.CategoriaMueble;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CrearDisenoMuebleRequest {
	
	@NotBlank(message = "El nombre del diseño es obligatorio")
    @Size(max = 150, message = "El nombre no puede superar 150 caracteres")
    private String nombre;

    @Size(max = 700, message = "La descripción no puede superar 700 caracteres")
    private String descripcion;

    @NotNull(message = "La categoría es obligatoria")
    private CategoriaMueble categoria;

    @NotBlank(message = "La imagen es obligatoria")
    private String imagenUrl;

    private String miniaturaUrl;
	
}
