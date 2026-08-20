package com.bolsadeideas.backend.muebleria.dtos;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class MuebleDTO {
	
	private Long id;
	private String descripcion;
	private BigDecimal precioReferencia;
	private Long disenoMuebleId;
	private String disenoMuebleNombre;
	private String disenoMiniaturaUrl;
	private Boolean activo;
}
