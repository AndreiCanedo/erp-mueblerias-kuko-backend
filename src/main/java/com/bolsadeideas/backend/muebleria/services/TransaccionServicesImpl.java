package com.bolsadeideas.backend.muebleria.services;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.backend.muebleria.dao.ITransaccionDao;
import com.bolsadeideas.backend.muebleria.dtos.TransaccionDTO;
import com.bolsadeideas.backend.muebleria.dtos.mappers.TransaccionMapper;
import com.bolsadeideas.backend.muebleria.model.Transaccion;
import com.bolsadeideas.backend.muebleria.response.ResponseBuilder;
import com.bolsadeideas.backend.muebleria.response.ResponseRest;
import com.bolsadeideas.backend.muebleria.response.ResponseRestObject;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TransaccionServicesImpl implements ITransaccionServices{

	@Autowired
	private ITransaccionDao transaccionDao;
	
	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<ResponseRest<TransaccionDTO>> search(int page, int size) {
		
		try {
			
			Page<Transaccion> pageResult = transaccionDao.findAll(
					PageRequest.of(
							page,
							size,
							Sort.by(Sort.Direction.DESC,"fecha")));
			
			List<TransaccionDTO> data = TransaccionMapper.toDTOList(pageResult.getContent());
			
			return ResponseBuilder.buildPageResponse(
					data,
					page,
					size,
					pageResult.getTotalElements(),
					pageResult.getTotalPages()
					);
			
		}catch(Exception e) {
			log.error("Error al consultar las transacciones: ",e);
			e.getStackTrace();
			return ResponseBuilder.buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "500", "Error en el Sistema");
		}
		
	}

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<ResponseRestObject<TransaccionDTO>> searchById(Long id) {
		try {
			
			return transaccionDao.findById(id)
					.map(transaccion -> ResponseBuilder.buildSuccessResponseObject(TransaccionMapper.toDTO(transaccion)))
					.orElseGet(() -> ResponseBuilder.buildErrorResponseObject(HttpStatus.NOT_FOUND, "404", "No se Encontro la transaccion"));
			
		}catch(Exception e) {
			log.error("Error al consultar las transacciones: ",e);
			e.getStackTrace();
			return ResponseBuilder.buildErrorResponseObject(HttpStatus.INTERNAL_SERVER_ERROR, "500", "Error en el Sistema");
		}
	}
	
}

