package com.bolsadeideas.backend.muebleria.services;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.bolsadeideas.backend.muebleria.dao.request.ActualizarMuebleRequest;
import com.bolsadeideas.backend.muebleria.dao.request.CrearMuebleRequest;
import com.bolsadeideas.backend.muebleria.dtos.MuebleDTO;
import com.bolsadeideas.backend.muebleria.response.ResponseRest;
import com.bolsadeideas.backend.muebleria.response.ResponseRestObject;



public interface IMuebleServices {
	
	public ResponseEntity<ResponseRest<MuebleDTO>> search();
	public ResponseEntity<ResponseRestObject<MuebleDTO>> searchById(Long id);
	public ResponseEntity<ResponseRest<MuebleDTO>> searchAllById(List<Long> id);
	public ResponseEntity<ResponseRest<MuebleDTO>> buscarMuebles(String texto);
	public ResponseEntity<ResponseRestObject<MuebleDTO>> save(CrearMuebleRequest request);
	public ResponseEntity<ResponseRestObject<MuebleDTO>> update(ActualizarMuebleRequest request, Long id);
	public ResponseEntity<ResponseRestObject<MuebleDTO>> deleteById(Long id);
	public ResponseEntity<ResponseRest<MuebleDTO>>searchByDisenoId(Long disenoId);
	public ResponseEntity<ResponseRestObject<MuebleDTO>> cambiarEstado(Long id, boolean activo);

}
