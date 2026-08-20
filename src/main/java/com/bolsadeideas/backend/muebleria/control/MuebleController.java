package com.bolsadeideas.backend.muebleria.control;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bolsadeideas.backend.muebleria.dao.request.ActualizarMuebleRequest;
import com.bolsadeideas.backend.muebleria.dao.request.CrearMuebleRequest;
import com.bolsadeideas.backend.muebleria.dtos.MuebleDTO;
import com.bolsadeideas.backend.muebleria.response.ResponseRest;
import com.bolsadeideas.backend.muebleria.response.ResponseRestObject;
import com.bolsadeideas.backend.muebleria.services.IMuebleServices;

@RestController
@RequestMapping("/api/v1/muebles")
public class MuebleController {
	
	private IMuebleServices muebleService;
	
	public MuebleController(IMuebleServices muebleService) {
		super();
		this.muebleService = muebleService;
	}
	
	@GetMapping
	public ResponseEntity<ResponseRest<MuebleDTO>> searchMueble(){
		return muebleService.search();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ResponseRestObject<MuebleDTO>> searchMuebleById(@PathVariable Long id){
		return muebleService.searchById(id);
	}
	
	@GetMapping("/buscar")
	public ResponseEntity<ResponseRest<MuebleDTO>> buscarMueble(@RequestParam String texto){
		return muebleService.buscarMuebles(texto);
	}
	
	@PostMapping
	public ResponseEntity<ResponseRestObject<MuebleDTO>> saveMueble(@RequestBody CrearMuebleRequest request){
		return muebleService.save(request);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ResponseRestObject<MuebleDTO>> saveMueble(@RequestBody ActualizarMuebleRequest request,@PathVariable Long id){
		return muebleService.update(request, id);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseRestObject<MuebleDTO>> deleteMueble(@PathVariable Long id){
		return muebleService.deleteById(id);
	}
	
	@GetMapping("/diseno/{disenoId}")
	public ResponseEntity<ResponseRest<MuebleDTO>>searchByDisenoId(@PathVariable Long disenoId) {
	    return muebleService.searchByDisenoId(disenoId);
	}
	
	@PatchMapping("/{id}/estado")
	public ResponseEntity<ResponseRestObject<MuebleDTO>>cambiarEstado(@PathVariable Long id, @RequestParam boolean activo) {
	    return muebleService.cambiarEstado(id, activo);
	}
	

}
