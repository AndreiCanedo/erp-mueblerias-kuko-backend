package com.bolsadeideas.backend.muebleria.services;


import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.backend.muebleria.dao.IEgresoDao;
import com.bolsadeideas.backend.muebleria.dao.request.ActualizarEgresoRequest;
import com.bolsadeideas.backend.muebleria.dao.request.CancelarEgresoRequest;
import com.bolsadeideas.backend.muebleria.dao.request.CrearEgresoRequest;
import com.bolsadeideas.backend.muebleria.dtos.EgresoDto;
import com.bolsadeideas.backend.muebleria.dtos.mappers.EgresoMapper;
import com.bolsadeideas.backend.muebleria.model.Egreso;
import com.bolsadeideas.backend.muebleria.model.EstadoEgreso;
import com.bolsadeideas.backend.muebleria.model.TipoReferencia;
import com.bolsadeideas.backend.muebleria.response.ResponseBuilder;
import com.bolsadeideas.backend.muebleria.response.ResponseRest;
import com.bolsadeideas.backend.muebleria.response.ResponseRestObject;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EgresoServicesImpl implements IEgresoServices{
	
	@Autowired
	private IEgresoDao egresoDao;
	
	@Autowired
	private FinanzasServices finanzasServices;

	
	//===================================================================//
	////////////////////////BUSCAR EGRESOS/////////////////////////////////
	//===================================================================//
	
	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<ResponseRest<EgresoDto>> search() {
		try {
			
			return ResponseBuilder.buildSuccessResponse(
					EgresoMapper.toDTOList(egresoDao.findAll(Sort.by(Sort.Direction.DESC,"id")))
					);
			
		}catch(Exception e) {
			log.error("Error al consultar las egreso: ",e);
			return ResponseBuilder.buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "500", "Error en el Sistema");
		}
	}
	
	//===================================================================//
	////////////////////////BUSCAR EGRESO//////////////////////////////////
	//===================================================================//
	
	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<ResponseRestObject<EgresoDto>> searchById(Long id) {
		try {
			
			return egresoDao.findById(id)
					.map(egreso -> ResponseBuilder.buildSuccessResponseObject(EgresoMapper.toDTO(egreso)))
					.orElseGet(() -> ResponseBuilder.buildErrorResponseObject(HttpStatus.NOT_FOUND, "404", "No se Encontro el Egreso"));
			
		}catch(Exception e) {
			log.error("Error al consultar las egreso: ",e);
			return ResponseBuilder.buildErrorResponseObject(HttpStatus.INTERNAL_SERVER_ERROR, "500", "Error en el Sistema");
		}
		
		
	}
	
	//===================================================================//
	////////////////////////GUARDAR EGRESO/////////////////////////////////
	//===================================================================//
	@Override
	@Transactional
	public ResponseEntity<ResponseRestObject<EgresoDto>> save(CrearEgresoRequest request) {
		try {
			
			/////////////////
			//VALIDACIONES///
			/////////////////
			
			String errorValidacion = validarCrearEgresoRequest(request);
			
			if(errorValidacion != null) {
				return ResponseBuilder.buildErrorResponseObject(HttpStatus.BAD_REQUEST, "400", errorValidacion);
			}
			
			Egreso egreso = new Egreso();
			
			egreso.setNombre(normalizarTexto(request.getNombre()));
			egreso.setMotivo(request.getMotivo().trim());
			egreso.setJustificacion(normalizarTexto(request.getJustificacion()));
			egreso.setEfectivoEntregado(normalizarImporte(request.getEfectivoEntregado()));
			egreso.setMonto(request.getMonto());
			egreso.setCambio(normalizarImporte(request.getCambio()));
			egreso.setFormaPago(request.getFormaPago());
			
			
			Egreso egresoSave = egresoDao.save(egreso);
			
			////////////////////////
			///REGISTRAR FINANZAS///
			////////////////////////
			
			finanzasServices.registrarEgreso(
					egresoSave.getMonto(),
					construirDescripcionEgreso(egresoSave),
					egresoSave.getId(),
					TipoReferencia.EGRESO
				);
			
			///////////////
			///RESPUESTA///
			///////////////
			
			return ResponseBuilder.buildSuccessResponseObject(EgresoMapper.toDTO(egresoSave));
			
		}catch(IllegalArgumentException e) {
			log.error("Solicitud invalida al guardar egreso: {} ",e.getMessage());
			return ResponseBuilder.buildErrorResponseObject(HttpStatus.BAD_REQUEST, "400", e.getMessage());
		}catch(Exception e) {
			log.error("Error al guardar egreso: ",e);
			return ResponseBuilder.buildErrorResponseObject(HttpStatus.INTERNAL_SERVER_ERROR, "500", "Error en el Sistema");
		}
	}

	//=======================================================================//
	////////////////////////ACTUALIZAR EGRESO/////////////////////////////////
	//=====================================================================//
	
	@Override
	@Transactional
	public ResponseEntity<ResponseRestObject<EgresoDto>> update(ActualizarEgresoRequest request, Long id) {
		try {
			
			/////////////////
			//VALIDACIONES///
			/////////////////
			
			String errorValidacion = validarActualizarEgresoRequest(request);
			
			if(errorValidacion != null) {
				return ResponseBuilder.buildErrorResponseObject(HttpStatus.BAD_REQUEST, "400", errorValidacion);
			}
			
	        ///////////////////
	        ///BUSCAR EGRESO///
	        ///////////////////
	        			
			Egreso egreso = egresoDao.findById(id).orElse(null);
			
			if(egreso == null) {
				return ResponseBuilder.buildErrorResponseObject(HttpStatus.NOT_FOUND, "404", "No se Encontro el Egreso");
			}
			
			if(egreso.getEstado() == EstadoEgreso.CANCELADO) {
				return ResponseBuilder.buildErrorResponseObject(HttpStatus.CONFLICT, "409", "El egreso ya esta cancelado");
			}
			
	        /////////////////////////
	        ///CALCULAR DIFERENCIA///
	        /////////////////////////			
			
			BigDecimal montoAnterior = egreso.getMonto();
			BigDecimal montoNuevo = request.getMonto();
			
			BigDecimal diferencia = montoNuevo.subtract(montoAnterior);
			
			if(diferencia.compareTo(BigDecimal.ZERO) != 0) {
				
				//Si el egreso aumenta, el impacto financiero es negativo, 
				//Si disminuye es positivo
				//BigDecimal impacto = diferencia.negate();
				
				finanzasServices.ajusteEgreso(
						diferencia,
						construirDescripcionAjusteEgreso(egreso, montoAnterior, montoNuevo),
						egreso.getId(),
						TipoReferencia.AJUSTE_UPDATE_EGRESO
						);
			}
			
	        //////////////////////
	        ///ACTUALIZAR DATOS///
	        //////////////////////			
			
			egreso.setNombre(normalizarTexto(request.getNombre()));
			egreso.setMotivo(request.getMotivo().trim());
			egreso.setJustificacion(normalizarTexto(request.getJustificacion()));
			egreso.setFormaPago(request.getFormaPago());
			egreso.setEfectivoEntregado(normalizarImporte(request.getEfectivoEntregado()));
			egreso.setMonto(montoNuevo);
			egreso.setCambio(normalizarImporte(request.getCambio()));
			
			Egreso egresoActualizado = egresoDao.save(egreso);
			
			return ResponseBuilder.buildSuccessResponseObject(EgresoMapper.toDTO(egresoActualizado));
		
		}catch(IllegalArgumentException e) {
			log.error("Solicitud invalida al actualizar egreso {}: {} ",id,e.getMessage());
			return ResponseBuilder.buildErrorResponseObject(HttpStatus.BAD_REQUEST, "400", e.getMessage());
			
		}catch(Exception e) {
			log.error("Error al actualizar egreso {} ",id,e);
			return ResponseBuilder.buildErrorResponseObject(HttpStatus.INTERNAL_SERVER_ERROR, "500", "Error en el Sistema");
		}
		
	
	}
	
	//=======================================================================//
	///////////////////////////CANCELAR EGRESO/////////////////////////////////
	//=======================================================================//

	
	@Override
	@Transactional
	public ResponseEntity<ResponseRestObject<EgresoDto>> cancelarEgreso(CancelarEgresoRequest request,Long id) {
		try {
			
			if(request == null || request.getMotivo() == null || request.getMotivo().isBlank()) {
				return ResponseBuilder.buildErrorResponseObject(HttpStatus.BAD_REQUEST, "400", "El motivo de cancelar es obligatorio");
			}
			
			Egreso egreso = egresoDao.findById(id).orElse(null);
			
			if(egreso == null) {
				return ResponseBuilder.buildErrorResponseObject(HttpStatus.NOT_FOUND, "404", "No se Encontro el Egreso");
			}
			
			if(egreso.getEstado() == EstadoEgreso.CANCELADO) {
				return ResponseBuilder.buildErrorResponseObject(HttpStatus.CONFLICT, "409", "El egreso ya esta cancelado");
			}
			
			
			////////////////////////
			///REGISTRAR FINANZAS///
			////////////////////////
				
			finanzasServices.ajusteEgreso(
					egreso.getMonto().negate(),
					"Cancelacion del egreso #: " + egreso.getId(),
					egreso.getId(),
					TipoReferencia.CANCELACION_EGRESO
	
					);
			
			egreso.setEstado(EstadoEgreso.CANCELADO);
			egreso.setFechaCancelacion(LocalDateTime.now());
			egreso.setMotivoCancelacion(request.getMotivo().trim());
			
			Egreso egresoCancelado = egresoDao.save(egreso);
			
			
			return ResponseBuilder.buildSuccessResponseObject(EgresoMapper.toDTO(egresoCancelado));
		}catch(Exception e) {
			log.error("Error al eliminar egreso: {}",id ,e);
			return ResponseBuilder.buildErrorResponseObject(HttpStatus.INTERNAL_SERVER_ERROR, "500", "Error en el Sistema");
		}
	}

	//==================================================================//
	////////////////////////VALIDACIONES/////////////////////////////////
	//================================================================//

	private String validarCrearEgresoRequest(CrearEgresoRequest request) {
		
		if(request == null) {
			return "Los datos del egreso son obligatorios";
		}
		
		if(request.getMotivo() == null || request.getMotivo().isBlank()) {
			return "El motivo es obligatorio";
		}
		
		if(request.getFormaPago() == null) {
			return "La forma de pago es obligatoria";
		}
		
		
		return validarImportes(request.getEfectivoEntregado(), request.getMonto(), request.getCambio());
		
	}
	
	private String validarActualizarEgresoRequest(ActualizarEgresoRequest request) {
		
		if(request == null) {
			return "Los datos del egreso son obligatorios";
		}
		
		if(request.getMotivo() == null || request.getMotivo().isBlank()) {
			return "El motivo es obligatorio";
		}
		
		if(request.getFormaPago() == null) {
			return "La forma de pago es obligatoria";
		}
		
		
		return validarImportes(request.getEfectivoEntregado(), request.getMonto(), request.getCambio());
		
	}
	
	private String validarImportes(BigDecimal efectivoEntregado, BigDecimal monto, BigDecimal cambio) {
		
		if(monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
			return "El monto debe ser mayor que cero";
		}
		
		if(efectivoEntregado != null && efectivoEntregado.compareTo(BigDecimal.ZERO) <= 0) {
			return "El efectivo entregado debe ser mayor a cero";
		}
		
		if(cambio != null && cambio.compareTo(BigDecimal.ZERO) < 0) {
			return "El cambio no puede ser negativo";
		}
		
		if(efectivoEntregado != null && monto.compareTo(efectivoEntregado) > 0) {
			return "El monto no puede ser mayor que el efectivo entregado";
		}
		
		
		//La igualdad se valida siempre y cuando los 3 datos estan presentes
		if(efectivoEntregado != null && cambio != null) {
			
			BigDecimal montoCalculado = efectivoEntregado.subtract(cambio);
			
			if(monto.compareTo(montoCalculado) != 0) {
				return "El monto debe ser igual al efectivo entregado menos el cambio";
			}
			
			
		}
		return null;
	}
	
	//==================================================================//
	//////////////////////////////HELPERS/////////////////////////////////
	//==================================================================//
	
	private String construirDescripcionEgreso(Egreso egreso) {
		return "Egreso #" + egreso.getId() + ": " + egreso.getMotivo();
	}
	
	private String construirDescripcionAjusteEgreso(Egreso egreso, BigDecimal montoAnterior, BigDecimal montoNuevo) {
		return "Ajuste Egreso #" + egreso.getId() + ". Monto anterior: " + montoAnterior + ", monto nuevo: "+ montoNuevo;
	}
	
	private String normalizarTexto(String valor) {
		if(valor == null || valor.isBlank()) {
			return null;
		}
		
		return valor.trim();
	}
	
	private BigDecimal normalizarImporte(BigDecimal valor) {
		if(valor == null) {
			return null;
		}
		
		return valor;
	}
}
