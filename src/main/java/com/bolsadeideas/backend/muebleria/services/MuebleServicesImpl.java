package com.bolsadeideas.backend.muebleria.services;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.backend.muebleria.dao.IDisenoMuebleDao;
import com.bolsadeideas.backend.muebleria.dao.IMuebleDao;
import com.bolsadeideas.backend.muebleria.dao.request.ActualizarMuebleRequest;
import com.bolsadeideas.backend.muebleria.dao.request.CrearMuebleRequest;
import com.bolsadeideas.backend.muebleria.dtos.MuebleDTO;
import com.bolsadeideas.backend.muebleria.dtos.mappers.MuebleMapper;
import com.bolsadeideas.backend.muebleria.model.DisenoMueble;
import com.bolsadeideas.backend.muebleria.model.Mueble;
import com.bolsadeideas.backend.muebleria.response.ResponseBuilder;
import com.bolsadeideas.backend.muebleria.response.ResponseRest;
import com.bolsadeideas.backend.muebleria.response.ResponseRestObject;

import lombok.extern.slf4j.Slf4j;

/**
 * Guarda un nuevo Mueble validando Precio Referencia y descripcion.
 * @param Mueble Datos del Mueble a guardar.
 * @return ResponseEntity con el mueble creado o error.
 */

@Service
@Slf4j
public class MuebleServicesImpl implements IMuebleServices{

	@Autowired
	private IMuebleDao muebleDao;
	
	@Autowired
	private IDisenoMuebleDao disenoMuebleDao;
	
	//===================================================================//
	////////////////////////BUSCAR MUEBLES/////////////////////////////////
	//===================================================================//	
	
	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<ResponseRest<MuebleDTO>> search() {
		try {
			return ResponseBuilder.buildSuccessResponse(
					MuebleMapper.toDTOList(muebleDao.findAll())
					);
		}catch(Exception e) {
			log.error("Error al consultar los muebles: ",e);
			return ResponseBuilder.buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "500", "Error en el Sistema");
		}
	
	}

	//===================================================================//
	////////////////////////BUSCAR MUEBLES POR ID//////////////////////////
	//===================================================================//	

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<ResponseRestObject<MuebleDTO>> searchById(Long id) {
		try {
			
			return  muebleDao.findById(id)
					.map(mueble -> ResponseBuilder.buildSuccessResponseObject(MuebleMapper.toDTO(mueble)))
					.orElseGet(() -> ResponseBuilder.buildErrorResponseObject(HttpStatus.NOT_FOUND, "404", "No se Encontro el mueble"));
		}catch(Exception e) {
			log.error("Error al consultar el mueble: ",e);
			return ResponseBuilder.buildErrorResponseObject(HttpStatus.INTERNAL_SERVER_ERROR, "500", "Error en el Sistema");
		}
	}
	
	//===================================================================//
	////////////////////////BUSCAR MUEBLES ALL POR ID//////////////////////
	//===================================================================//	
	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<ResponseRest<MuebleDTO>> searchAllById(List<Long> id) {
		try {
			return ResponseBuilder.buildSuccessResponse(
					MuebleMapper.toDTOList(muebleDao.findAllById(id))
					);
		}catch(Exception e) {
			log.error("Error al consultar los muebles: ",e);
			return ResponseBuilder.buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "500", "Error en el Sistema");
		}
	}
	
	//===================================================================//
	////////////////////////BUSCAR MUEBLES POR TEXTO///////////////////////
	//===================================================================//	

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<ResponseRest<MuebleDTO>> buscarMuebles(String texto) {
		try {
			
			if(texto == null || texto.trim().isEmpty()) {
				return ResponseBuilder.buildSuccessResponse(List.of());
			}
			
			String textoNormalizado = texto.trim();
			
			return ResponseBuilder.buildSuccessResponse(
					MuebleMapper.toDTOList(muebleDao.buscarMuebles(textoNormalizado)));
			
		}catch(Exception e) {
			log.error("Error al consultar los muebles: ",e);
			return ResponseBuilder.buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "500", "Error en el Sistema");
		}
	}
	
	//===================================================================//
	////////////////////////GUARDAR MUEBLES////////////////////////////////
	//===================================================================//	
	
	@Override
	@Transactional
	public ResponseEntity<ResponseRestObject<MuebleDTO>> save(CrearMuebleRequest request) {
		try {
			
				// Validaciones básicas
		        String error = validarMuebleCrear(request);
		        
		        if(error != null) {
		        	return ResponseBuilder.buildErrorResponseObject(HttpStatus.BAD_REQUEST, "400", error);
		        }
		        
		        DisenoMueble diseno = null;
		        
		        if(request.getDisenoMuebleId() != null) {
		        	diseno = disenoMuebleDao.findById(request.getDisenoMuebleId()).orElse(null);
		        	
		        	if (diseno == null) {
		                return ResponseBuilder
		                		.buildErrorResponseObject(HttpStatus.NOT_FOUND, "404","No se encontró el diseño del mueble");
		        	}
		        	
		        	if (!Boolean.TRUE.equals(diseno.getActivo())) {
		                return ResponseBuilder
		                    .buildErrorResponseObject(HttpStatus.BAD_REQUEST, "400", "El diseño seleccionado está inactivo");
		            }
		        }
		        
		        Mueble mueble = new Mueble();
		        mueble.setDescripcion(normalizarTexto(request.getDescripcion()));
		        mueble.setPrecioReferencia(request.getPrecioReferencia());
		        mueble.setDisenoMueble(diseno);
		        mueble.setActivo(true);
		      
		        Mueble muebleSave = muebleDao.save(mueble);
		        
				return ResponseBuilder.buildSuccessResponseObject(MuebleMapper.toDTO(muebleSave));
				
		}catch(Exception e) {
			log.error("Error al guardar mueble: ",e);
			return ResponseBuilder.buildErrorResponseObject(HttpStatus.INTERNAL_SERVER_ERROR, "500", "Error en el Sistema");
		}
	}
	
	//===================================================================//
	////////////////////////ACTUALIZAR MUEBLES/////////////////////////////
	//===================================================================//	

	@Override
	@Transactional
	public ResponseEntity<ResponseRestObject<MuebleDTO>> update(ActualizarMuebleRequest request, Long id) {
		try {
			
			// Validaciones básicas
	        String error = validarMuebleActualizacion(request, id);
	        
	        if(error != null) {
	        	return ResponseBuilder.buildErrorResponseObject(HttpStatus.BAD_REQUEST, "400", error);
	        }
	        
	        Mueble muebleExistente = muebleDao.findById(id).orElse(null);
	        
	        if(muebleExistente == null) {
	        	return ResponseBuilder
	        			.buildErrorResponseObject(HttpStatus.NOT_FOUND, "404", "No se encontro el mueble");
	        }
	        
	        DisenoMueble diseno = null;

	        if (request.getDisenoMuebleId() != null) {
	        	diseno = disenoMuebleDao.findById(request.getDisenoMuebleId()).orElse(null);

	            if (diseno == null) {
	                return ResponseBuilder
	                    .buildErrorResponseObject(HttpStatus.NOT_FOUND, "404", "No se encontró el diseño del mueble");
	            }
	        }

	        muebleExistente.setDescripcion(normalizarTexto(request.getDescripcion()));
	        muebleExistente.setPrecioReferencia(request.getPrecioReferencia());
	        muebleExistente.setDisenoMueble(diseno);
	        muebleExistente.setActivo(request.getActivo());
	        
	        Mueble muebleSave = muebleDao.save(muebleExistente);
	        
	        return ResponseBuilder.buildSuccessResponseObject(MuebleMapper.toDTO(muebleSave));
			
		}catch(Exception e) {
			log.error("Error al actualizar mueble: ",e);
			return ResponseBuilder.buildErrorResponseObject(HttpStatus.INTERNAL_SERVER_ERROR, "500", "Error en el Sistema");
		}
	}
	
	//===================================================================//
	////////////////////////ELIMINAR MUEBLES///////////////////////////////
	//===================================================================//	

	@Override
	@Transactional
	public ResponseEntity<ResponseRestObject<MuebleDTO>> deleteById(Long id) {
		try {
			return muebleDao.findById(id)
					.map(muebleSearch -> {
						muebleSearch.setActivo(false);
						
						Mueble muebleSave = muebleDao.save(muebleSearch);
						
						return ResponseBuilder.buildSuccessResponseObject(MuebleMapper.toDTO(muebleSave));
					})
					.orElseGet(() -> ResponseBuilder.buildErrorResponseObject(HttpStatus.NOT_FOUND, "404", "No se Encontro el mueble"));
		}catch(Exception e) {
			log.error("Error al desactivar el mueble: ",e);
			return ResponseBuilder.buildErrorResponseObject(HttpStatus.INTERNAL_SERVER_ERROR, "500", "Error en el Sistema");
		}
	}
	
	//===================================================================//
	///////////////////////BUSCAR POR MUEBLE DISEÑO////////////////////////
	//===================================================================//	
	
	@Override
	@Transactional
	public ResponseEntity<ResponseRestObject<MuebleDTO>> cambiarEstado(Long id, boolean activo) {
	    try {

	        return muebleDao.findById(id)
	            .map(mueble -> {

	                mueble.setActivo(activo);

	                Mueble guardado = muebleDao.save(mueble);

	                return ResponseBuilder.buildSuccessResponseObject(MuebleMapper.toDTO(guardado));
	            })
	            .orElseGet(() ->
	                ResponseBuilder.buildErrorResponseObject(HttpStatus.NOT_FOUND, "404","No se encontró el mueble")
	            );

	    } catch (Exception e) {

	        log.error("Error al cambiar el estado del mueble: ", e);

	        return ResponseBuilder.buildErrorResponseObject( HttpStatus.INTERNAL_SERVER_ERROR,"500","Error en el Sistema");
	    }
	}
	
	//===================================================================//
	///////////////////////BUSCAR POR MUEBLE DISEÑO////////////////////////
	//===================================================================//	
	
	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<ResponseRest<MuebleDTO>>searchByDisenoId(Long disenoId) {

	    try {

	        if (!disenoMuebleDao.existsById(disenoId)) {
	            return ResponseBuilder.buildErrorResponse(HttpStatus.NOT_FOUND, "404","No se encontró el diseño del mueble");
	        }

	        return ResponseBuilder.buildSuccessResponse(
	            MuebleMapper.toDTOList( muebleDao.findByDisenoMuebleIdAndActivoTrueOrderByDescripcionAsc(disenoId)));

	    } catch (Exception e) {

	        log.error("Error al consultar muebles por diseño: ", e);

	        return ResponseBuilder.buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "500", "Error en el Sistema");
	    }
	}
	
	
	//===================================================================//
	///////////////////////////////VALIDACIONES////////////////////////////
	//===================================================================//	

	private String validarMuebleCrear(CrearMuebleRequest request) {
		
		if(request == null) {
			return "Los datos del mueble son obligatorios";
		}
		
		String descripcion = normalizarTexto(request.getDescripcion());
		
		if(descripcion == null) {
			
			return "La descripcion es obligatoria";
			
		}
		
		if (muebleDao.existsByDescripcionIgnoreCase(descripcion)) {
		    return "Ya existe un mueble con esa descripción";
		}

		if (request.getPrecioReferencia() != null && request.getPrecioReferencia().compareTo(BigDecimal.ZERO) < 0){
		        return "El precio de referencia no puede ser negativo";
		}

		request.setDescripcion(descripcion);

		return null;
		
	}
	
	private String validarMuebleActualizacion(ActualizarMuebleRequest request, Long muebleId) {
		
		if (request == null) {
	        return "Los datos del mueble son obligatorios";
	    }

	    String descripcion = normalizarTexto(request.getDescripcion());

	    if (descripcion == null) {
	        return "La descripción es obligatoria";
	    }

	    if (muebleDao.existsByDescripcionIgnoreCaseAndIdNot(descripcion,muebleId)) {
	        return "Ya existe otro mueble con esa descripción";
	    }

	    if (request.getPrecioReferencia() != null && request.getPrecioReferencia().compareTo(BigDecimal.ZERO) < 0) {
	        return "El precio de referencia no puede ser negativo";
	    }

	    if (request.getActivo() == null) {
	        return "El estado del mueble es obligatorio";
	    }

	    request.setDescripcion(descripcion);

	    return null;
		
	}
	
	//===================================================================//
	///////////////////////////////HELPERS/////////////////////////////////
	//===================================================================//	
	
	public void actualizarDatosMueble(Mueble muebleExistente, MuebleDTO muebleNuevo) {
		
		if (muebleNuevo.getDescripcion() != null) muebleExistente.setDescripcion(muebleNuevo.getDescripcion());
		if (muebleNuevo.getPrecioReferencia() != null) muebleExistente.setPrecioReferencia(muebleNuevo.getPrecioReferencia());
	}
	
	private String normalizarTexto(String valor) {
		if(valor == null || valor.isBlank()) {
			return null;
		}
		
		return valor.trim();
	}
}
