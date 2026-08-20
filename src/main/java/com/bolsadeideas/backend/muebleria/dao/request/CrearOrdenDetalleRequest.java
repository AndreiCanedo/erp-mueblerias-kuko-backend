package com.bolsadeideas.backend.muebleria.dao.request;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CrearOrdenDetalleRequest {
	
	private Long muebleId;
	
	private Double cantidad;
	
	private BigDecimal precioUnitario;
	
}
