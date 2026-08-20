package com.bolsadeideas.backend.muebleria.dao.request;

import java.math.BigDecimal;

import com.bolsadeideas.backend.muebleria.model.FormaPago;

import lombok.Data;

@Data
public class ActualizarEgresoRequest {
	
	private String nombre;
	private String motivo;
	private String justificacion;
	
	private BigDecimal efectivoEntregado;
	private BigDecimal monto;
	private BigDecimal cambio;

	private FormaPago formaPago;
}
	

