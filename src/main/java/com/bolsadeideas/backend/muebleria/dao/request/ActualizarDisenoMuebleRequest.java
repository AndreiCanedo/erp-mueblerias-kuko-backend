package com.bolsadeideas.backend.muebleria.dao.request;

import com.bolsadeideas.backend.muebleria.model.CategoriaMueble;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ActualizarDisenoMuebleRequest {
	
	@NotBlank(message = "El nombre del diseño es obligatorio")
    @Size(max = 150)
    private String nombre;

    @Size(max = 700)
    private String descripcion;

    @NotNull(message = "La categoría es obligatoria")
    private CategoriaMueble categoria;

    @NotBlank(message = "La imagen es obligatoria")
    private String imagenUrl;

    private String miniaturaUrl;

    @NotNull(message = "El estado es obligatorio")
    private Boolean activo;
	
}
