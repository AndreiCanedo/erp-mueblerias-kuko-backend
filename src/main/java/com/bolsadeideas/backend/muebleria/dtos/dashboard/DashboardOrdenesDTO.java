package com.bolsadeideas.backend.muebleria.dtos.dashboard;

import java.util.List;

import lombok.Data;

@Data
public class DashboardOrdenesDTO {
	
	private Long pendientes;
	private Long produccion;
	private Long listasParaEntregar;
	private Long atrasadas;
	private List<DashboardOrdenResumenDTO> proximas;
	
}
