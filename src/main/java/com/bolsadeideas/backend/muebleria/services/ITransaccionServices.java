package com.bolsadeideas.backend.muebleria.services;

import org.springframework.http.ResponseEntity;

import com.bolsadeideas.backend.muebleria.dtos.TransaccionDTO;
import com.bolsadeideas.backend.muebleria.response.ResponseRest;
import com.bolsadeideas.backend.muebleria.response.ResponseRestObject;

public interface ITransaccionServices {
	
	public ResponseEntity<ResponseRest<TransaccionDTO>> search(int page, int size);
	public ResponseEntity<ResponseRestObject<TransaccionDTO>> searchById(Long id);

}
