package com.bolsadeideas.backend.muebleria.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bolsadeideas.backend.muebleria.dtos.TransaccionDTO;
import com.bolsadeideas.backend.muebleria.response.ResponseRest;
import com.bolsadeideas.backend.muebleria.response.ResponseRestObject;
import com.bolsadeideas.backend.muebleria.services.ITransaccionServices;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")
public class TransaccionController {
	
	@Autowired
	private ITransaccionServices services;

	@GetMapping("/transacciones")
	public ResponseEntity<ResponseRest<TransaccionDTO>> searchTransaccion(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size){
		return services.search(page,size);
	}
	
	@GetMapping("/transacciones/{id}")
	public ResponseEntity<ResponseRestObject<TransaccionDTO>> searchByIdTransaccion(@PathVariable Long id){
		return services.searchById(id);
	}
	
}
