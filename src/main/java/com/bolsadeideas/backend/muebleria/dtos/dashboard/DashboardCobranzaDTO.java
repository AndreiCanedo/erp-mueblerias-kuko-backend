package com.bolsadeideas.backend.muebleria.dtos.dashboard;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class DashboardCobranzaDTO {
	
	private BigDecimal totalVendido;
	private BigDecimal totalCobrado;
	private BigDecimal saldoPendiente;
	private BigDecimal porcentajeCobrado;

}
