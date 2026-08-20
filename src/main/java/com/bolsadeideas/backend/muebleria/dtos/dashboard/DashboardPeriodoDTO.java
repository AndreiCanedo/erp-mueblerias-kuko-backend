package com.bolsadeideas.backend.muebleria.dtos.dashboard;

import java.time.LocalDate;

import lombok.Data;

@Data
public class DashboardPeriodoDTO {
	
	private LocalDate inicio;
	private LocalDate fin;
	private LocalDate inicioAnterior;
	private LocalDate finAnterior;

}
