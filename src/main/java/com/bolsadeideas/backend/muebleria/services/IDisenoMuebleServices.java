package com.bolsadeideas.backend.muebleria.services;

import org.springframework.http.ResponseEntity;

import com.bolsadeideas.backend.muebleria.dao.request.ActualizarDisenoMuebleRequest;
import com.bolsadeideas.backend.muebleria.dao.request.CrearDisenoMuebleRequest;
import com.bolsadeideas.backend.muebleria.dtos.DisenoMuebleDTO;
import com.bolsadeideas.backend.muebleria.model.CategoriaMueble;
import com.bolsadeideas.backend.muebleria.response.ResponseRest;
import com.bolsadeideas.backend.muebleria.response.ResponseRestObject;

public interface IDisenoMuebleServices {

	ResponseEntity<ResponseRest<DisenoMuebleDTO>> search();
	ResponseEntity<ResponseRest<DisenoMuebleDTO>> searchActivos();
	ResponseEntity<ResponseRestObject<DisenoMuebleDTO>> searchById(Long id);
	ResponseEntity<ResponseRest<DisenoMuebleDTO>> searchByCategoria(CategoriaMueble categoria);
	ResponseEntity<ResponseRest<DisenoMuebleDTO>> buscar(String texto);
	ResponseEntity<ResponseRestObject<DisenoMuebleDTO>>	save(CrearDisenoMuebleRequest request);
	ResponseEntity<ResponseRestObject<DisenoMuebleDTO>> update(Long id, ActualizarDisenoMuebleRequest request);
	ResponseEntity<ResponseRestObject<DisenoMuebleDTO>> cambiarEstado(Long id, boolean activo);
	
}
