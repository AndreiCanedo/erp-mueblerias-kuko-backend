package com.bolsadeideas.backend.muebleria.control;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bolsadeideas.backend.muebleria.dtos.dashboard.DashboardDTO;
import com.bolsadeideas.backend.muebleria.response.ResponseRestObject;
import com.bolsadeideas.backend.muebleria.services.IDashBoardServices;

@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {
	
	@Autowired
	private IDashBoardServices dashboardServices;
	
	@GetMapping
	public ResponseEntity<ResponseRestObject<DashboardDTO>> obtenerDashboard(
			@RequestParam
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
			LocalDate inicio,
			
			@RequestParam
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
			LocalDate fin){
		
		return dashboardServices.obtenerDashboard(inicio, fin);
	}

}
