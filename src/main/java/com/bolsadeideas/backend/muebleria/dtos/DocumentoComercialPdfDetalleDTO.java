package com.bolsadeideas.backend.muebleria.dtos;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentoComercialPdfDetalleDTO {
	
	private Double cantidad;

    private Long muebleId;

    private String descripcion;

    private BigDecimal precioUnitario;

    private BigDecimal precioTotal;
	
}
