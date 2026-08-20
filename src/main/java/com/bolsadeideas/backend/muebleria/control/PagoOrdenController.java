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

import com.bolsadeideas.backend.muebleria.dao.request.ActualizarPagoOrdenRequest;
import com.bolsadeideas.backend.muebleria.dao.request.CancelarPagoOrdenRequest;
import com.bolsadeideas.backend.muebleria.dao.request.CrearPagoOrdenRequest;
import com.bolsadeideas.backend.muebleria.dtos.PagoOrdenDTO;
import com.bolsadeideas.backend.muebleria.response.ResponseRest;
import com.bolsadeideas.backend.muebleria.response.ResponseRestObject;
import com.bolsadeideas.backend.muebleria.services.IPagoOrdenServices;

@RestController
@RequestMapping("/api/v1/pagos-orden")
public class PagoOrdenController {
	
	@Autowired
	private IPagoOrdenServices pagoServices;
	
	@GetMapping
	public ResponseEntity<ResponseRest<PagoOrdenDTO>> searchPagos(){
		return 	pagoServices.searchPagos();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ResponseRestObject<PagoOrdenDTO>> searchPagoById(@PathVariable Long id){
		return pagoServices.searchPagoById(id);
	}

	@GetMapping("/orden/{ordenId}")
	public ResponseEntity<ResponseRest<PagoOrdenDTO>> searchPagosByOrdenId(@PathVariable Long ordenId){
		return pagoServices.searchPagosByOrdenId(ordenId);
	}
	
	@PostMapping
	public ResponseEntity<ResponseRestObject<PagoOrdenDTO>> savePago(@RequestBody CrearPagoOrdenRequest request){
		return pagoServices.savePago(request);
	}
	
	@PutMapping("/{id}/datos")
	public ResponseEntity<ResponseRestObject<PagoOrdenDTO>> updateIngreso(@RequestBody ActualizarPagoOrdenRequest request, @PathVariable Long id){
		return pagoServices.updateDatosPago(request, id);
	}
	
	@DeleteMapping("/{id}/cancelar")
	public ResponseEntity<ResponseRestObject<PagoOrdenDTO>> cancelarPago(@RequestBody CancelarPagoOrdenRequest request, @PathVariable Long id){
		return pagoServices.cancelarPago(request,id);
	}

}
