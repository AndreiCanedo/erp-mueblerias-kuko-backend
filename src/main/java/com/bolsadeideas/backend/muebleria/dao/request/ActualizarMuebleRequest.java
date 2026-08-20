package com.bolsadeideas.backend.muebleria.dao.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ActualizarMuebleRequest {
	
	@NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    @DecimalMin(value = "0.0", inclusive = true, message = "El precio no puede ser negativo")
    private BigDecimal precioReferencia;

    private Long disenoMuebleId;

    @NotNull(message = "El estado es obligatorio")
    private Boolean activo;
	
}
