package com.bolsadeideas.backend.muebleria.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bolsadeideas.backend.muebleria.dao.request.ActualizarOrdenCompraRequest;
import com.bolsadeideas.backend.muebleria.dao.request.CancelarOrdenCompraRequest;
import com.bolsadeideas.backend.muebleria.dao.request.CrearOrdenCompraRequest;
import com.bolsadeideas.backend.muebleria.dtos.OrdenCompraDTO;
import com.bolsadeideas.backend.muebleria.response.ResponseRest;
import com.bolsadeideas.backend.muebleria.response.ResponseRestObject;
import com.bolsadeideas.backend.muebleria.services.IOrdenCompraServices;

@RestController
@RequestMapping("/api/v1/ordenes")
public class OrdenCompraController {
	
	@Autowired
	private IOrdenCompraServices services;
	
	@GetMapping
	public ResponseEntity<ResponseRest<OrdenCompraDTO>> searchOrdenCompra(){
		return services.searchOrdenCompra();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ResponseRestObject<OrdenCompraDTO>> searchOrdenCompraById(@PathVariable Long id){
		return services.searchOrdenCompraById(id);
	}
	
	@PostMapping("")
	public ResponseEntity<ResponseRestObject<OrdenCompraDTO>> saveOrdenCompra(@RequestBody CrearOrdenCompraRequest request){
		return services.saveOrdenCompra(request);
	}
	
	@PatchMapping("/{id}/confirmar")
	public ResponseEntity<ResponseRestObject<OrdenCompraDTO>> confirmarOrdenCompra(@PathVariable Long id) {

	    return services.confirmarOrdenCompra(id);
	}
	
	
	@PutMapping("/{id}")
	public ResponseEntity<ResponseRestObject<OrdenCompraDTO>> updateOrdenCompra(
			@PathVariable Long id, 
			@RequestBody ActualizarOrdenCompraRequest request){
		return services.updateOrdenCompra(request, id);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseRestObject<OrdenCompraDTO>> deleteOrdenCompra(@PathVariable Long id){
		return services.deleteOrdenCompraById(id);
	}
	
	@PatchMapping("/{id}/cancelar")
	public ResponseEntity<ResponseRestObject<OrdenCompraDTO>> cancelarOrdenCompra(@RequestBody CancelarOrdenCompraRequest request, @PathVariable Long id){
		return services.cancelarOrdenCompra(request, id);
	}

}
