package com.bolsadeideas.backend.muebleria.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.backend.muebleria.dao.IPagoOrdenDao;
import com.bolsadeideas.backend.muebleria.dao.request.ActualizarPagoOrdenRequest;
import com.bolsadeideas.backend.muebleria.dao.request.CancelarPagoOrdenRequest;
import com.bolsadeideas.backend.muebleria.dao.request.CrearPagoOrdenRequest;
import com.bolsadeideas.backend.muebleria.dao.IOrdenCompraDao;
import com.bolsadeideas.backend.muebleria.dtos.PagoOrdenDTO;
import com.bolsadeideas.backend.muebleria.dtos.mappers.PagoOrdenMapper;
import com.bolsadeideas.backend.muebleria.model.EstadoOrdenCompra;
import com.bolsadeideas.backend.muebleria.model.EstadoPago;
import com.bolsadeideas.backend.muebleria.model.EstadoPagoOrden;
import com.bolsadeideas.backend.muebleria.model.PagoOrden;
import com.bolsadeideas.backend.muebleria.model.OrdenCompra;
import com.bolsadeideas.backend.muebleria.model.TipoPagoOrden;
import com.bolsadeideas.backend.muebleria.model.TipoReferencia;
import com.bolsadeideas.backend.muebleria.response.ResponseBuilder;
import com.bolsadeideas.backend.muebleria.response.ResponseRest;
import com.bolsadeideas.backend.muebleria.response.ResponseRestObject;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PagoOrdenServicesImpl implements IPagoOrdenServices{
	
	@Autowired
	private IPagoOrdenDao pagoOrdenDao;
	
	@Autowired
	private IOrdenCompraDao ordenDao;
	
	@Autowired
	private FinanzasServices finanzasServices;

	//===================================================================//
	////////////////////////BUSCAR PAGOS///////////////////////////////////
	//===================================================================//
	
	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<ResponseRest<PagoOrdenDTO>> searchPagos() {
		try {
			
			return ResponseBuilder.buildSuccessResponse(
					PagoOrdenMapper.toDTOList(pagoOrdenDao.findAll())
					);
			
		}catch(Exception e) {
			log.error("Error al consultar los pagos de las ordenes: ",e);
			return ResponseBuilder.buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "500", "Error en el Sistema");
		}
	}

	//===================================================================//
	////////////////////////BUSCAR PAGOS DE UNA ORDEN//////////////////////
	//===================================================================//
	
	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<ResponseRest<PagoOrdenDTO>> searchPagosByOrdenId(Long ordenId) {
		try {
			
			if(!ordenDao.existsById(ordenId)) {
				return ResponseBuilder.buildErrorResponse(HttpStatus.NOT_FOUND, "404", "No se encontro la orden de compra");
			}
			
			return ResponseBuilder.buildSuccessResponse(PagoOrdenMapper.toDTOList(
					pagoOrdenDao.findByOrdenIdOrderByFechaRegistroAsc(ordenId)
					));
			
		}catch(Exception e) {
			log.error("Error al consultar los pagos de una orden {}: ",ordenId,e);
			return ResponseBuilder.buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "500", "Error en el Sistema");
		}
	}
	
	//===================================================================//
	////////////////////////BUSCAR PAGOS DE UNA ORDEN//////////////////////
	//===================================================================//
		
	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<ResponseRestObject<PagoOrdenDTO>> searchPagoById(Long id) {
		try {
				
			return pagoOrdenDao.findById(id)
					.map(pago -> ResponseBuilder.buildSuccessResponseObject(PagoOrdenMapper.toDTO(pago)))
					.orElseGet(() -> ResponseBuilder.buildErrorResponseObject(HttpStatus.NOT_FOUND, "404", "No se encontro el Pago"));
				
		}catch(Exception e) {
			log.error("Error al consultar el pago {}: ",id,e);
			return ResponseBuilder.buildErrorResponseObject(HttpStatus.INTERNAL_SERVER_ERROR, "500", "Error en el Sistema");
		}
	}

	//===================================================================//
	///////////////////////////GUARDAR PAGO////////////////////////////////
	//===================================================================//
	
	@Override
	@Transactional
	public ResponseEntity<ResponseRestObject<PagoOrdenDTO>> savePago(CrearPagoOrdenRequest request) {
		try {
			
			/////////////////
			//VALIDACIONES///
			/////////////////
			
			String errorValidacion = validarCrearPagoRequest(request);
			
			if(errorValidacion != null) {
				return ResponseBuilder.buildErrorResponseObject(HttpStatus.BAD_REQUEST, "400", errorValidacion);
			}
			
			///////////////////
			///OPTENER ORDEN///
			///////////////////
			
			OrdenCompra orden = ordenDao.findById(request.getOrdenId())
					.orElse(null);
			
			if(orden == null) {
				return ResponseBuilder.buildErrorResponseObject(HttpStatus.NOT_FOUND, "404", "No se encontro la orden de compra");
			}
			
			
			String errorOrden = validarOrdenAceptaPagos(orden);
			
			if(errorOrden != null) {
				return ResponseBuilder.buildErrorResponseObject(HttpStatus.BAD_REQUEST, "400", errorOrden);
			}
			
			////////////////////
			///VALIDAR EXCESO///
			////////////////////

			BigDecimal totalPagado = obtenerTotalPagado(orden.getId());
			BigDecimal saldoPendiente = orden.getTotal().subtract(totalPagado);
			
			if(request.getMonto().compareTo(saldoPendiente) > 0) {
				return ResponseBuilder.buildErrorResponseObject(HttpStatus.BAD_REQUEST, "400", "El saldo excede el pago pendiente. Saldo: "+saldoPendiente);
			}
			
			TipoPagoOrden tipoPago = determinarTipoPago(totalPagado,request.getMonto(), orden.getTotal());
			
			//////////////////////
			///CREAR ORDEN PAGO///
			//////////////////////
			
			PagoOrden pago = new PagoOrden();
			
			pago.setOrden(orden);
			pago.setMonto(request.getMonto());
			pago.setFormaPago(request.getFormaPago());
			pago.setTipoPago(tipoPago);
			pago.setEstado(EstadoPagoOrden.APLICADO);
			pago.setReferencia(normalizarTexto(request.getReferencia()));
			pago.setObservaciones(normalizarTexto(request.getObservaciones()));
			
			PagoOrden pagoGuardado = pagoOrdenDao.save(pago);
			
			BigDecimal nuevoTotalPagado = totalPagado.add(pagoGuardado.getMonto());
			
			//////////////////////
			///ACTUALIZAR ORDEN///
			//////////////////////			
			
			actualizarEstadoPagoOrden(orden, nuevoTotalPagado);
			
			ordenDao.save(orden);
			
			////////////////////////
			///REGISTRAR FINANZAS///
			////////////////////////
			
			
			finanzasServices.registrarIngreso(
						pagoGuardado.getMonto(), 
						construirDescripcionPago(pagoGuardado), 
						pagoGuardado.getId(), 
						TipoReferencia.PAGO_ORDEN);
	        
			
			///////////////
			///RESPUESTA///
			///////////////
			
			return ResponseBuilder.buildSuccessResponseObject(PagoOrdenMapper.toDTO(pagoGuardado));	
			
			
		}catch(Exception e) {
			log.error("Error al guardar el pago de la orden",e);
			return ResponseBuilder.buildErrorResponseObject(HttpStatus.INTERNAL_SERVER_ERROR, "500", "Error en el Sistema");
		}
	}

	
	//===================================================================//
	////////////////////////ACTUALIZAR INGRESO/////////////////////////////
	//===================================================================//
	
	@Override
	@Transactional
	public ResponseEntity<ResponseRestObject<PagoOrdenDTO>> updateDatosPago(ActualizarPagoOrdenRequest request, Long id) {
		try {
			
			/////////////////
			//VALIDACIONES///
			/////////////////
			
			if(request == null) {
				return ResponseBuilder.buildErrorResponseObject(HttpStatus.BAD_REQUEST, "400", "Los datos del pago son obligatorios");
			}
			
			PagoOrden pago = pagoOrdenDao.findById(id).orElse(null);
			
			if(pago == null) {
				return ResponseBuilder.buildErrorResponseObject(HttpStatus.NOT_FOUND, "404", "No se encontro el pago");
			}
			
			
			if(pago.getEstado() == EstadoPagoOrden.CANCELADO) {
				return ResponseBuilder.buildErrorResponseObject(HttpStatus.BAD_REQUEST, "400", "No se puede modificar un pago cancelado");
			}
			
			if(request.getFormaPago() == null) {
				return ResponseBuilder.buildErrorResponseObject(HttpStatus.BAD_REQUEST, "400", "La forma de pago es obligatoria");
			}
						
			/////////////////////////////////
			///ACTUALIZAR DATOS ORDEN PAGO///
			/////////////////////////////////
			
			pago.setFormaPago(request.getFormaPago());
			pago.setReferencia(normalizarTexto(request.getReferencia()));
			pago.setObservaciones(normalizarTexto(request.getObservaciones()));
			
			PagoOrden pagoActualizado = pagoOrdenDao.save(pago);
						
			///////////////
			///RESPUESTA///
			///////////////
									
			return  ResponseBuilder.buildSuccessResponseObject(PagoOrdenMapper.toDTO(pagoActualizado));
			
		}catch(Exception e) {
			log.error("Error al actualizar los datos del pago {}",id,e);
			return ResponseBuilder.buildErrorResponseObject(HttpStatus.INTERNAL_SERVER_ERROR, "500", "Error en el Sistema");
		}
	}

	//===================================================================//
	//////////////////////CANCELAR ORDEN PAGO//////////////////////////////
	//===================================================================//
	
	@Override
	@Transactional
	public ResponseEntity<ResponseRestObject<PagoOrdenDTO>> cancelarPago(CancelarPagoOrdenRequest request, Long id) {
		try {
			
			/////////////////
			//VALIDACIONES///
			/////////////////
			
			if(request == null || request.getMotivo() == null || request.getMotivo().isBlank()) {
				return ResponseBuilder.buildErrorResponseObject(HttpStatus.BAD_REQUEST, "400", "El motivo es obligatorio");
			}
			
			PagoOrden pago = pagoOrdenDao.findById(id).orElse(null);
			
			if(pago == null) {
				return ResponseBuilder.buildErrorResponseObject(HttpStatus.NOT_FOUND, "404", "No se encontro el pago");
			}
			
			if(pago.getEstado() == EstadoPagoOrden.CANCELADO) {
				return ResponseBuilder.buildErrorResponseObject(HttpStatus.BAD_REQUEST, "400", "El pago ya esta cancelado");
			}
			
			/////////////////////////////////
			///ACTUALIZAR DATOS ORDEN PAGO///
			/////////////////////////////////			
			
			OrdenCompra orden = pago.getOrden();
			
			pago.setEstado(EstadoPagoOrden.CANCELADO);
			pago.setFechaCancelacion(LocalDateTime.now());
			pago.setMotivoCancelacion(request.getMotivo().trim());
			
			PagoOrden pagoCancelado = pagoOrdenDao.save(pago);
			
			BigDecimal totalPagadoAplicado = obtenerTotalPagado(orden.getId());
			
			//////////////////////
			///ACTUALIZAR ORDEN///
			//////////////////////			
			
			actualizarEstadoPagoOrden(orden, totalPagadoAplicado);
			
			ordenDao.save(orden);
			
			////////////////////////
			///REGISTRAR FINANZAS///
			////////////////////////
			
			finanzasServices.ajusteIngreso(
					pagoCancelado.getMonto().negate(), 
					"Cancelacion de pago de orden #" + orden.getId() + ", pago #" + pagoCancelado.getId(), 
					pagoCancelado.getId(), 
					TipoReferencia.CANCELACION_PAGO_ORDEN);
			
			///////////////
			///RESPUESTA///
			///////////////
			
			return ResponseBuilder.buildSuccessResponseObject(PagoOrdenMapper.toDTO(pagoCancelado));
			
		}catch(Exception e) {
			log.error("Error al eliminar Ingreso: ",e);
			return ResponseBuilder.buildErrorResponseObject(HttpStatus.INTERNAL_SERVER_ERROR, "500", "Error en el Sistema");
		}
	}
	
	
	//===================================================================//
	////////////////////////////VALIDACIONES///////////////////////////////
	//===================================================================//
	
	private String validarCrearPagoRequest(CrearPagoOrdenRequest request) {
		if(request == null) {
			return "Los datos del pago son obligatorios";
		}
		
		if(request.getOrdenId() == null) {
			return "La orden de compra es obligatoria";
		}
		
		 if(request.getMonto() == null || request.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
			return "El monto debe ser mayor de 0";
		}
		 
		if(request.getFormaPago() == null) {
			return "La forma de pago es obligatoria";
		}
		
		return null;
	}
	
	private String validarOrdenAceptaPagos(OrdenCompra orden) {
		
		if(orden.getEstadoOrden() == EstadoOrdenCompra.CANCELADA) {
			return "No se puede registrar Pagos a una orden Cancelada";
		}
		
		if(orden.getEstadoOrden() != EstadoOrdenCompra.CONFIRMADA) {
			return "Solo las ordenes confirmadas pueden recibir Pagos";
		}
		
		if(orden.getTotal() == null || orden.getTotal().compareTo(BigDecimal.ZERO) <= 0) {
			return "La orden no tiene un total valido";
		}
		
		
		return null;
	}
	
	//===================================================================//
	//////////////////////////////CALCULOS/////////////////////////////////
	//===================================================================//
	
	
	private BigDecimal obtenerTotalPagado(Long ordenId) {
		
		BigDecimal total = pagoOrdenDao.sumMontoByOrdenIdAndEstado(ordenId, EstadoPagoOrden.APLICADO);
		
		return total != null ? total : BigDecimal.ZERO;
		
	}
	
	private TipoPagoOrden determinarTipoPago(BigDecimal totalPagadoAnterior, BigDecimal nuevoPago, BigDecimal totalOrden) {
		
		BigDecimal nuevoTotalPagado = totalPagadoAnterior.add(nuevoPago);
		
		//el nuevo pago completa exactamente la orden
		if(nuevoTotalPagado.compareTo(totalOrden) == 0) {
			return TipoPagoOrden.LIQUIDACION;
		}
		
		//No habia pagos anteriores es el primer pago parcial
		if(totalPagadoAnterior.compareTo(BigDecimal.ZERO) == 0) {
			return TipoPagoOrden.ANTICIPO;
		}
		
		//ya existe pago y todavia queda saldo
		return TipoPagoOrden.ABONO;
		
	}
	
	private void actualizarEstadoPagoOrden(OrdenCompra orden, BigDecimal totalPagado) {
		
		if(totalPagado == null || totalPagado.compareTo(BigDecimal.ZERO) <= 0) {
			orden.setEstadoPago(EstadoPago.SIN_PAGO);
			return;
		}
		
		if(totalPagado.compareTo(orden.getTotal()) >= 0) {
			orden.setEstadoPago(EstadoPago.PAGADA);
			return;
		}
		
		orden.setEstadoPago(EstadoPago.PAGO_PARCIAL);
	}
	
	//===================================================================//
	////////////////////////////UTILIDADES/////////////////////////////////
	//===================================================================//
	
	private String construirDescripcionPago(PagoOrden pago) {
		
		return pago.getTipoPago() + " de orden #" + pago.getOrden().getId() + ", pago #" + pago.getId();
				
	}
	
	private String normalizarTexto(String valor) {
		if(valor == null || valor.isBlank()) {
			return null;
		}
		
		return valor.trim();
	}
	
	
}
