package com.bolsadeideas.backend.muebleria.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bolsadeideas.backend.muebleria.dtos.MuebleriaDTO;
import com.bolsadeideas.backend.muebleria.response.ResponseRestObject;
import com.bolsadeideas.backend.muebleria.services.IMuebleriaServices;


@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")
public class MuebleriaController {
	
	@Autowired
	private IMuebleriaServices service;
	

	@GetMapping("/muebleria")
	public ResponseEntity<ResponseRestObject<MuebleriaDTO>> getEstado(){
		return service.getEstado();
	}
	
}
