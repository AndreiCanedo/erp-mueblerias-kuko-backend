package com.bolsadeideas.backend.muebleria.dtos.dashboard;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class DashboardFinanzaPuntoDTO {
	
	private LocalDate fecha;
	private BigDecimal ingresos;
	private BigDecimal egresos;
	private BigDecimal balance;
	
}
