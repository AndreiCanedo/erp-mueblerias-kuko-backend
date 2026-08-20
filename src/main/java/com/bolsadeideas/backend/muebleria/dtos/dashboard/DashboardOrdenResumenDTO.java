package com.bolsadeideas.backend.muebleria.dtos.dashboard;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class DashboardOrdenResumenDTO {
	
	private Long id;
	private Long clienteId;
	private String cliente;
	private BigDecimal total;
	private LocalDate fechaEntrega;
	private String estadoOrden;
	private String proceso;
	private String estadoPago;
	private String estadoEntrega;
	
}
