package com.bolsadeideas.backend.muebleria.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.backend.muebleria.dao.IDisenoMuebleDao;
import com.bolsadeideas.backend.muebleria.dao.request.ActualizarDisenoMuebleRequest;
import com.bolsadeideas.backend.muebleria.dao.request.CrearDisenoMuebleRequest;
import com.bolsadeideas.backend.muebleria.dtos.DisenoMuebleDTO;
import com.bolsadeideas.backend.muebleria.dtos.mappers.DisenoMuebleMapper;
import com.bolsadeideas.backend.muebleria.model.CategoriaMueble;
import com.bolsadeideas.backend.muebleria.model.DisenoMueble;
import com.bolsadeideas.backend.muebleria.response.ResponseBuilder;
import com.bolsadeideas.backend.muebleria.response.ResponseRest;
import com.bolsadeideas.backend.muebleria.response.ResponseRestObject;


import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DisenoMuebleImpl implements IDisenoMuebleServices{
	
	
	@Autowired
	private IDisenoMuebleDao disenoMuebleDao;
	
	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<ResponseRest<DisenoMuebleDTO>> search() {
		try {
			return ResponseBuilder.buildSuccessResponse(
				DisenoMuebleMapper.toDTOList(disenoMuebleDao.findAll())
			);
			
		}catch(Exception e) {
			log.error("Error al consultar los diseños de muebles: ",e);
			return ResponseBuilder.buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "500", "Error en el Sistema");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<ResponseRest<DisenoMuebleDTO>> searchActivos() {
		try {
			return ResponseBuilder.buildSuccessResponse(
				DisenoMuebleMapper.toDTOList(
					disenoMuebleDao.findByActivoTrueOrderByFechaRegistroDesc()
				)
			);
			
		}catch(Exception e) {
			log.error("Error al consultar los diseños de muebles: ",e);
			return ResponseBuilder.buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "500", "Error en el Sistema");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<ResponseRestObject<DisenoMuebleDTO>> searchById(Long id) {
		try {
			return disenoMuebleDao.findById(id)
					.map(disenoMueble -> ResponseBuilder.buildSuccessResponseObject(DisenoMuebleMapper.toDTO(disenoMueble)))
					.orElseGet(() -> ResponseBuilder.buildErrorResponseObject(HttpStatus.NOT_FOUND, "404", "No se Encontro el diseño del mueble"));
			
		}catch(Exception e) {
			log.error("Error al consultar el diseño del mueble: ",e);
			return ResponseBuilder
					.buildErrorResponseObject(HttpStatus.INTERNAL_SERVER_ERROR, "500", "Error en el Sistema");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<ResponseRest<DisenoMuebleDTO>> searchByCategoria(CategoriaMueble categoria) {
		try {
			if(categoria == null) {
				return ResponseBuilder.buildSuccessResponse(List.of());
			}
			
			return ResponseBuilder.buildSuccessResponse(
				DisenoMuebleMapper.toDTOList(
					disenoMuebleDao.findByCategoriaAndActivoTrueOrderByFechaRegistroDesc(categoria)
				)
			);
			
		}catch(Exception e) {
			log.error("Error al consultar el diseño del mueble por categoria: ",e);
			return ResponseBuilder.buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "500", "Error en el Sistema");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<ResponseRest<DisenoMuebleDTO>> buscar(String texto) {
		try {
			if(texto == null || texto.trim().isEmpty()) {
				return ResponseBuilder.buildSuccessResponse(List.of());
			}
			
			String textoNormalizado = texto.trim();
			
			return ResponseBuilder.buildSuccessResponse(
				DisenoMuebleMapper.toDTOList(disenoMuebleDao.buscarActivos(textoNormalizado))
			);
			
		}catch(Exception e) {
			log.error("Error al consultar el diseño del mueble por busqueda: ",e);
			return ResponseBuilder.buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "500", "Error en el Sistema");
		}
	}

	@Override
	@Transactional
	public ResponseEntity<ResponseRestObject<DisenoMuebleDTO>> save(CrearDisenoMuebleRequest request) {
		try {
			
			String error = validarDisenoMuebleCrear(request);
			
			if(error != null) {
				return ResponseBuilder.buildErrorResponseObject(HttpStatus.BAD_REQUEST, "400", error);
			}
			
			DisenoMueble disenoM = new DisenoMueble();
			
			disenoM.setNombre(normalizarTexto(request.getNombre()));
			disenoM.setDescripcion(normalizarTexto(request.getDescripcion()));
			disenoM.setCategoria(request.getCategoria());
			disenoM.setImagenUrl(request.getImagenUrl());
			disenoM.setMiniaturaUrl(request.getMiniaturaUrl());
			
			DisenoMueble disenoMuebleSave = disenoMuebleDao.save(disenoM);
			
			return ResponseBuilder.buildSuccessResponseObject(DisenoMuebleMapper.toDTO(disenoMuebleSave));
			
		}catch(Exception e) {
			log.error("Error al guardar el diseño del mueble: ",e);
			return ResponseBuilder
					.buildErrorResponseObject(HttpStatus.INTERNAL_SERVER_ERROR, "500", "Error en el Sistema");
		}
	}

	@Override
	@Transactional
	public ResponseEntity<ResponseRestObject<DisenoMuebleDTO>> update(Long id, ActualizarDisenoMuebleRequest request) {
		try {
			
			String error = validarDisenoMuebleActualizar(request, id);
			
			if(error != null) {
				return ResponseBuilder.buildErrorResponseObject(HttpStatus.BAD_REQUEST, "400", error);
			}
			
			DisenoMueble dm = disenoMuebleDao.findById(id).orElse(null);
			
			if(dm == null) {
				return ResponseBuilder.buildErrorResponseObject(HttpStatus.NOT_FOUND, "404", "No se encontró el diseño del mueble");
			}
			
			dm.setNombre(normalizarTexto(request.getNombre()));
			dm.setDescripcion(normalizarTexto(request.getDescripcion()));
			dm.setCategoria(request.getCategoria());
			dm.setImagenUrl(request.getImagenUrl());
			dm.setMiniaturaUrl(request.getMiniaturaUrl());
			dm.setActivo(request.getActivo());
			
			DisenoMueble dmSave = disenoMuebleDao.save(dm);
			
			return ResponseBuilder.buildSuccessResponseObject(DisenoMuebleMapper.toDTO(dmSave));
			
		} catch (Exception e){
			log.error("Error al actualizar el diseño del mueble: ",e);
			return ResponseBuilder
					.buildErrorResponseObject(HttpStatus.INTERNAL_SERVER_ERROR, "500", "Error en el Sistema");
		}
	}

	@Override
	@Transactional
	public ResponseEntity<ResponseRestObject<DisenoMuebleDTO>> cambiarEstado(Long id, boolean activo) {
		try {
			
			return disenoMuebleDao.findById(id).map(diseno -> {

				diseno.setActivo(activo);

		        DisenoMueble disenoGuardado = disenoMuebleDao.save(diseno);

		        return ResponseBuilder.buildSuccessResponseObject(
		        		DisenoMuebleMapper.toDTO(disenoGuardado));
			})
		    .orElseGet(() ->
		        ResponseBuilder
		        	.buildErrorResponseObject( HttpStatus.NOT_FOUND, "404", "No se encontró el diseño del mueble"));
			
		}catch(Exception e) {
			log.error("Error al consultar el diseño del mueble: ",e);
			return ResponseBuilder
					.buildErrorResponseObject(HttpStatus.INTERNAL_SERVER_ERROR, "500", "Error en el Sistema");
		}
	}
	
	private String validarDisenoMuebleCrear(CrearDisenoMuebleRequest request) {
		
		if(request == null) {
			return "Los datos de diseño mueble son obligatorios";
		}
		
		String nombre = normalizarTexto(request.getNombre());

		if (nombre == null) {
		    return "El nombre del diseño es obligatorio";
		}
		
		if (disenoMuebleDao.existsByNombreIgnoreCase(nombre)) {
		    return "Ya existe un diseño con ese nombre";
		 }
		
		if(request.getCategoria() == null) {
			return "La categoria del diseño es obligatoria";
		}
		
		 String imagenUrl = normalizarTexto(request.getImagenUrl());
		
		if(imagenUrl == null) {
			return "La imagen del diseño es obligatorio";
		}
		
		return null;
		
	}
	
	private String validarDisenoMuebleActualizar(ActualizarDisenoMuebleRequest request, Long id) {
		
		if (request == null) {
	        return "Los datos del diseño del mueble son obligatorios";
	    }

	    String nombre = normalizarTexto(request.getNombre());

	    if (nombre == null) {
	        return "El nombre del diseño es obligatorio";
	    }

	    if (disenoMuebleDao.existsByNombreIgnoreCaseAndIdNot(nombre,id)) {
	        return "Ya existe otro diseño con ese nombre";
	    }

	    if (request.getCategoria() == null) {
	        return "La categoría del diseño es obligatoria";
	    }

	    String imagenUrl = normalizarTexto(request.getImagenUrl());

	    if (imagenUrl == null) {
	        return "La imagen del diseño es obligatoria";
	    }

	    if (request.getActivo() == null) {
	        return "El estado del diseño es obligatorio";
	    }
		
		return null;
	}
	
	private String normalizarTexto(String valor) {
		if(valor == null || valor.isBlank()) {
			return null;
		}
		
		return valor.trim();
	}
	
}
