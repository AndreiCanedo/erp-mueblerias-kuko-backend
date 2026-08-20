package com.bolsadeideas.backend.muebleria.dtos.dashboard;

import java.util.List;

import lombok.Data;

@Data
public class DashboardDTO {
	
	private DashboardPeriodoDTO periodo;
	private DashboardKpisDTO kpis;
	private List<DashboardFinanzaPuntoDTO> finanzas;
	private DashboardCobranzaDTO cobranza;
	private DashboardOrdenesDTO ordenes;
	
}
