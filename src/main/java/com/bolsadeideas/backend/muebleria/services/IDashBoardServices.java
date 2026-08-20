package com.bolsadeideas.backend.muebleria.services;

import java.time.LocalDate;

import org.springframework.http.ResponseEntity;

import com.bolsadeideas.backend.muebleria.dtos.dashboard.DashboardDTO;
import com.bolsadeideas.backend.muebleria.response.ResponseRestObject;

public interface IDashBoardServices {
	
	ResponseEntity<ResponseRestObject<DashboardDTO>> obtenerDashboard (LocalDate inicio, LocalDate fin);
	
}
