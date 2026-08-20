package com.bolsadeideas.backend.muebleria.dtos.dashboard;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class DashboardKpisDTO {

	private BigDecimal ingresos;
	private BigDecimal egresos;
	private BigDecimal balance;
	
	private BigDecimal ventas;
	private BigDecimal saldoPendiente;
	
	private Long ordenesActivas;
	
	private BigDecimal tendenciaIngresos;
	private BigDecimal tendenciaVentas;
	private BigDecimal tendenciaEgresos;
	
}
