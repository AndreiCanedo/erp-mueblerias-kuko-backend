package com.bolsadeideas.backend.muebleria.services;

import org.springframework.http.ResponseEntity;

import com.bolsadeideas.backend.muebleria.dtos.MuebleriaDTO;
import com.bolsadeideas.backend.muebleria.response.ResponseRestObject;

public interface IMuebleriaServices {
	
	public ResponseEntity<ResponseRestObject<MuebleriaDTO>> getEstado();
	
}
