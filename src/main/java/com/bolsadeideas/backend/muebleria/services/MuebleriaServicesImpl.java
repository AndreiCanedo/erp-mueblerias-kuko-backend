package com.bolsadeideas.backend.muebleria.services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.backend.muebleria.dao.IMuebleriaDao;
import com.bolsadeideas.backend.muebleria.dtos.MuebleriaDTO;
import com.bolsadeideas.backend.muebleria.dtos.mappers.MuebleriaMapper;
import com.bolsadeideas.backend.muebleria.model.Muebleria;
import com.bolsadeideas.backend.muebleria.response.ResponseBuilder;
import com.bolsadeideas.backend.muebleria.response.ResponseRestObject;

import lombok.RequiredArgsConstructor;

/**
 * Guarda una nueva Muebleria.
 * @param Muebleria Datos de la muebleria a guardar.
 * @return ResponseEntity con la muebleria creada o error.
 */

@Service
@RequiredArgsConstructor
public class MuebleriaServicesImpl implements IMuebleriaServices{
	

	private final IMuebleriaDao muebleriaDao;
	
	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<ResponseRestObject<MuebleriaDTO>> getEstado() {
		
		Muebleria m = muebleriaDao.findById(1L).
				orElseThrow(() -> new RuntimeException("No existe muebleria"));
		
		return ResponseBuilder.buildSuccessResponseObject(
				MuebleriaMapper.toDTO(m)
		);
	}


}
