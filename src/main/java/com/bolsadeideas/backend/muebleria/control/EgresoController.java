package com.bolsadeideas.backend.muebleria.control;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bolsadeideas.backend.muebleria.dao.request.ActualizarEgresoRequest;
import com.bolsadeideas.backend.muebleria.dao.request.CancelarEgresoRequest;
import com.bolsadeideas.backend.muebleria.dao.request.CrearEgresoRequest;
import com.bolsadeideas.backend.muebleria.dtos.EgresoDto;
import com.bolsadeideas.backend.muebleria.response.ResponseRest;
import com.bolsadeideas.backend.muebleria.response.ResponseRestObject;
import com.bolsadeideas.backend.muebleria.services.IEgresoServices;

@RestController
@RequestMapping("/api/v1/egresos")
public class EgresoController {
	
	@Autowired
	private IEgresoServices egresoService;
	

	@PostMapping	
	public ResponseEntity<ResponseRestObject<EgresoDto>> saveMuebleria(@RequestBody CrearEgresoRequest request){	
		//System.out.println("Egreso recibido: " + egreso);
		return egresoService.save(request);
	}
	
	@GetMapping("/{id}")	
	public ResponseEntity<ResponseRestObject<EgresoDto>> searchById(@PathVariable Long id){	
	
		return egresoService.searchById(id);
	}
	
	@GetMapping
	public ResponseEntity<ResponseRest<EgresoDto>> searchAll(){
		return egresoService.search();
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ResponseRestObject<EgresoDto>> updateEgreso(@RequestBody ActualizarEgresoRequest request, @PathVariable Long id){
		
		return egresoService.update(request, id);
	}
	
	@PatchMapping("/{id}/cancelar")
	public ResponseEntity<ResponseRestObject<EgresoDto>> cancelarEgreso(@RequestBody CancelarEgresoRequest request, @PathVariable Long id){
		
		return egresoService.cancelarEgreso(request,id);
	}
	
	
}
