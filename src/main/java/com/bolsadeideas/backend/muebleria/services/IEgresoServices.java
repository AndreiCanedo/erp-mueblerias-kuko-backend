package com.bolsadeideas.backend.muebleria.services;

import org.springframework.http.ResponseEntity;

import com.bolsadeideas.backend.muebleria.dao.request.ActualizarEgresoRequest;
import com.bolsadeideas.backend.muebleria.dao.request.CancelarEgresoRequest;
import com.bolsadeideas.backend.muebleria.dao.request.CrearEgresoRequest;
import com.bolsadeideas.backend.muebleria.dtos.EgresoDto;
import com.bolsadeideas.backend.muebleria.response.ResponseRest;
import com.bolsadeideas.backend.muebleria.response.ResponseRestObject;

public interface IEgresoServices {
	
	public ResponseEntity<ResponseRest<EgresoDto>> search();
	public ResponseEntity<ResponseRestObject<EgresoDto>> searchById(Long id);
	public ResponseEntity<ResponseRestObject<EgresoDto>> save(CrearEgresoRequest request);
	public ResponseEntity<ResponseRestObject<EgresoDto>> update(ActualizarEgresoRequest request, Long id);
	public ResponseEntity<ResponseRestObject<EgresoDto>> cancelarEgreso(CancelarEgresoRequest request, Long id);

}
