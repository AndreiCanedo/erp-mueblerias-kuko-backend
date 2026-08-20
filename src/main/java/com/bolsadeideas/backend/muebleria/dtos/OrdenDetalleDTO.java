package com.bolsadeideas.backend.muebleria.dtos;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class OrdenDetalleDTO {
	
	private Long id;
	private Double cantidad;
	private BigDecimal precioUnitario;
	private BigDecimal PrecioTotal;
	
	private Long ordenID;
	private Long muebleID;
	

	
}
