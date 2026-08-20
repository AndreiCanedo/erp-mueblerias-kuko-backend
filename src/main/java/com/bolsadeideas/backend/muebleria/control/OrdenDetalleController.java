package com.bolsadeideas.backend.muebleria.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bolsadeideas.backend.muebleria.dtos.OrdenDetalleDTO;
import com.bolsadeideas.backend.muebleria.response.ResponseRest;
import com.bolsadeideas.backend.muebleria.response.ResponseRestObject;
import com.bolsadeideas.backend.muebleria.services.IOrdenDetalleServices;

@RestController
@RequestMapping("/api/v1")
public class OrdenDetalleController {
	
	@Autowired
	private IOrdenDetalleServices odServices;
	
	@GetMapping("/detalles")
	public ResponseEntity<ResponseRest<OrdenDetalleDTO>> searchOrdenDetalle(){
		return odServices.searchOrdenDetalle();
	}
	
	@GetMapping("/detalle/{id}")
	public ResponseEntity<ResponseRestObject<OrdenDetalleDTO>> searchOrdenDetalleById(@PathVariable Long id){
		return odServices.searchOrdenDetalleById(id);
	}
	
	@PostMapping("/detalle")
	public ResponseEntity<ResponseRestObject<OrdenDetalleDTO>> saveOrdenDetalle(@RequestBody OrdenDetalleDTO odDTO){
		return odServices.saveOrdenDetalle(odDTO);
	}
	
	@PutMapping("/detalle/{id}")
	public ResponseEntity<ResponseRestObject<OrdenDetalleDTO>> updateOrdenDetalle(@PathVariable Long id, @RequestBody OrdenDetalleDTO odDTO){
		return odServices.updateOrdenDetalle(odDTO, id);
	}
	
	@DeleteMapping("/detalle/{id}")
	public ResponseEntity<ResponseRestObject<OrdenDetalleDTO>> deleteOrdenDetalle(@PathVariable Long id){
		return odServices.deleteOrdenDetalleById(id);
	}
	
}
