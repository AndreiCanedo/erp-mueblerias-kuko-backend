package com.bolsadeideas.backend.muebleria.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.backend.muebleria.dao.IClienteDao;
import com.bolsadeideas.backend.muebleria.dao.IMuebleDao;
import com.bolsadeideas.backend.muebleria.dao.IOrdenCompraDao;
import com.bolsadeideas.backend.muebleria.dao.request.ActualizarOrdenCompraRequest;
import com.bolsadeideas.backend.muebleria.dao.request.CancelarOrdenCompraRequest;
import com.bolsadeideas.backend.muebleria.dao.request.CrearOrdenCompraRequest;
import com.bolsadeideas.backend.muebleria.dao.request.CrearOrdenDetalleMapper;
import com.bolsadeideas.backend.muebleria.dao.request.CrearOrdenDetalleRequest;
import com.bolsadeideas.backend.muebleria.dtos.OrdenCompraDTO;
import com.bolsadeideas.backend.muebleria.dtos.mappers.OrdenCompraMapper;
import com.bolsadeideas.backend.muebleria.model.Cliente;
import com.bolsadeideas.backend.muebleria.model.EstadoEntrega;
import com.bolsadeideas.backend.muebleria.model.EstadoOrdenCompra;
import com.bolsadeideas.backend.muebleria.model.EstadoPago;
import com.bolsadeideas.backend.muebleria.model.OrdenCompra;
import com.bolsadeideas.backend.muebleria.model.OrdenDetalle;
import com.bolsadeideas.backend.muebleria.model.Proceso;
import com.bolsadeideas.backend.muebleria.response.ResponseBuilder;
import com.bolsadeideas.backend.muebleria.response.ResponseRest;
import com.bolsadeideas.backend.muebleria.response.ResponseRestObject;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrdenCompraServicesImpl implements IOrdenCompraServices{
	
	@Autowired
	private IOrdenCompraDao ordenDao;
	
	@Autowired
	private IClienteDao clienteDao;
	
	@Autowired
	private IMuebleDao muebleDao;
	
	//===================================================================//
	////////////////////////BUSCAR ORDEN///////////////////////////////////
	//===================================================================//
	
	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<ResponseRest<OrdenCompraDTO>> searchOrdenCompra() {
		try {
			
			return ResponseBuilder.buildSuccessResponse(
					OrdenCompraMapper.toDTOList(ordenDao.findAll())
					);
		}catch(Exception e) {
			log.error("Error al consultar la Orden de Compra: ",e);
			return ResponseBuilder.buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "500", "Error en el Sistema");
		}
	}
	
	//===================================================================//
	////////////////////////BUSCAR ORDEN BY ID/////////////////////////////
	//===================================================================//

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<ResponseRestObject<OrdenCompraDTO>> searchOrdenCompraById(Long id) {
		try {
			return ordenDao.findById(id)
	                .map(orden -> ResponseBuilder.buildSuccessResponseObject(OrdenCompraMapper.toDTO(orden)))
	                .orElseGet(() -> ResponseBuilder.buildErrorResponseObject(HttpStatus.NOT_FOUND, "404", "Orden de compra no encontrada"));
			}catch(Exception e) {
				log.error("Error al consultar la Orden de Compra: ",e);
				return ResponseBuilder.buildErrorResponseObject(HttpStatus.INTERNAL_SERVER_ERROR, "500", "Error en el Sistema");
			}
	}

	//===================================================================//
	////////////////////////GUARDAR ORDEN/////////////////////////////////
	//===================================================================//
	
	@Override
	@Transactional
	public ResponseEntity<ResponseRestObject<OrdenCompraDTO>> saveOrdenCompra(CrearOrdenCompraRequest request) {
		try {
			
			///////////////////
			///VALIDACIONES////
			///////////////////
			OrdenCompra orden = new OrdenCompra();
			
			//Validar request
			validarRequest(request.getClienteId(), request.getDetallesRequest());
			
			validarFechaEntregaCrear(request.getFechaEntrega());
			
			//////////////////
			/////ASIGNAR//////
			//////////////////
			
			//Asignar y guardado de Cliente
			asignarCliente(request.getClienteId(), orden);

			
			//Asignar estados iniciales
			orden.setEstadoOrden(EstadoOrdenCompra.COTIZACION);
			orden.setProceso(Proceso.SIN_INICIAR);
			orden.setEstadoPago(EstadoPago.SIN_PAGO);
			orden.setEstadoEntrega(EstadoEntrega.PROGRAMADA);
			orden.setFechaEntrega(request.getFechaEntrega());
			
			
			//////////////
			///GUARDAR////
			//////////////
			
			//Guardar Detalles
			guardarDetalles(request.getDetallesRequest(), orden);

			//Asignar y guardar Detalles y precio Total
			
			asignarDetalleCalcularTotal(orden);
			
			OrdenCompra ordenCompraFinal = ordenDao.save(orden);
			
			
			return ResponseBuilder.buildSuccessResponseObject(
					OrdenCompraMapper.toDTO(ordenCompraFinal)
					);
			
		}catch(IllegalArgumentException e) {
			log.error("Solicitud invalidada al crear la orden: {}",e.getMessage());
			return ResponseBuilder.buildErrorResponseObject(HttpStatus.BAD_REQUEST, "400", "No se pudo guardar la Orden");
			
		}catch(Exception e) {
			log.error("Error al guardar la Orden de Compra: ",e);
			return ResponseBuilder.buildErrorResponseObject(HttpStatus.INTERNAL_SERVER_ERROR, "500", "Error en el Sistema");
		}
	}
	
	
	//===================================================================//
	////////////////////////CONFIRMAR ORDEN////////////////////////////////
	//===================================================================//	
	@Override
	@Transactional
	public ResponseEntity<ResponseRestObject<OrdenCompraDTO>> confirmarOrdenCompra(Long id) {
		try {
			///////////////////
			///VALIDACIONES////
			///////////////////			
			OrdenCompra orden = ordenDao.findById(id).orElse(null);
			
			if(orden == null) {
				
				return ResponseBuilder.buildErrorResponseObject(HttpStatus.NOT_FOUND, "404", "Orden de compra no encontrada");
			
			}
			
			if (orden.getEstadoOrden() != EstadoOrdenCompra.COTIZACION) {

	            throw new IllegalArgumentException("Solamente una cotización puede confirmarse");
	        }

	        if (orden.getDetalles() == null || orden.getDetalles().isEmpty()) {

	            throw new IllegalArgumentException("La orden debe tener al menos un detalle");
	        }

	        if (orden.getTotal() == null || orden.getTotal().compareTo(BigDecimal.ZERO) <= 0) {

	            throw new IllegalArgumentException("El total de la orden debe ser mayor que cero");
	        }
	        
			//////////////////
			/////ASIGNAR//////
			//////////////////
				        
	        orden.setEstadoOrden(EstadoOrdenCompra.CONFIRMADA);
	        orden.setFechaConfirmacion(LocalDateTime.now());
	        
			//////////////
			///GUARDAR////
			//////////////	        
	        
	        OrdenCompra ordenConfirmada = ordenDao.save(orden);
	        
	        return ResponseBuilder.buildSuccessResponseObject(OrdenCompraMapper.toDTO(ordenConfirmada));
			
		}catch(IllegalArgumentException e) {
			log.error("No se Pudo confirmar la orden {} : {}", id, e.getMessage());
			return ResponseBuilder.buildErrorResponseObject(HttpStatus.BAD_REQUEST, "400", e.getMessage());
			
		}catch(Exception e) {
			log.error("Error al guardar la Orden de Compra: ",e);
			return ResponseBuilder.buildErrorResponseObject(HttpStatus.INTERNAL_SERVER_ERROR, "500", "Error en el Sistema");
		}
		
	}
	

	//===================================================================//
	////////////////////////ACTUALIZAR ORDEN///////////////////////////////
	//===================================================================//
	@Override
	@Transactional
	public ResponseEntity<ResponseRestObject<OrdenCompraDTO>> updateOrdenCompra(
			ActualizarOrdenCompraRequest request, Long id) {
		
		try {
			///////////////////
			///VALIDACIONES////
			///////////////////			
			OrdenCompra orden = ordenDao.findById(id).orElse(null);
			
			if(orden == null) {
				return ResponseBuilder.buildErrorResponseObject(HttpStatus.NOT_FOUND, "404", "Orden de compra no encontrada");
			}
			
			
			validarOrdenEditable(orden);
			
			validarRequest(request.getClienteId(), request.getDetallesRequest());
			
			validarFechaEntregaActualizacion(request.getFechaEntrega(), orden);
			
			if(orden.getCliente() == null || !orden.getCliente().getId().equals(request.getClienteId())) {
				asignarCliente(request.getClienteId(),orden);
			}
			
			//////////////////
			/////ASIGNAR//////
			//////////////////
			
			reemplazarDetalles(request.getDetallesRequest(), orden);
			
			asignarDetalleCalcularTotal(orden);
			
			// Estos campos son opcionales durante la actualización.
	        if (request.getFechaEntrega() != null && orden.getEstadoEntrega() == EstadoEntrega.PENDIENTE) {
	            orden.setEstadoEntrega(EstadoEntrega.PROGRAMADA);
	        }
	        
	        orden.setFechaEntrega(request.getFechaEntrega());

			//////////////
			///GUARDAR////
			//////////////	        
	        
	        OrdenCompra ordenActualizada = ordenDao.save(orden);
	        
	        return ResponseBuilder.buildSuccessResponseObject(OrdenCompraMapper.toDTO(ordenActualizada));
			
			}catch(IllegalArgumentException e) {
			log.error("Solicitud invalidada al actualizar la orden {} : {}",id,e.getMessage());
			return ResponseBuilder.buildErrorResponseObject(HttpStatus.BAD_REQUEST, "400", e.getMessage());
		
			}catch(Exception e) {
			log.error("Error al Actualizar la Orden de Compra con ID: {} ",id,e);
			return ResponseBuilder.buildErrorResponseObject(HttpStatus.INTERNAL_SERVER_ERROR, "500", "Error en el Sistema");
		}
	}

	//===================================================================//
	////////////////////////ELIMINAR ORDEN/////////////////////////////////
	//===================================================================//
	@Override
	@Transactional
	public ResponseEntity<ResponseRestObject<OrdenCompraDTO>> deleteOrdenCompraById(Long id) {
		try {
			///////////////////
			///VALIDACIONES////
			///////////////////			
			OrdenCompra orden = ordenDao.findById(id).orElse(null);
			
			if(orden == null) {
				return ResponseBuilder.buildErrorResponseObject(HttpStatus.NOT_FOUND, "404", "No se encontro la orden de compra");
			}
			
			validarOrdenEliminable(orden);
			
			//////////////
			///GUARDAR////
			//////////////
						
			OrdenCompraDTO ordenEliminada = OrdenCompraMapper.toDTO(orden);
			
			//No ocupo eliminar detalle porque lo hace hibarnate ediante cascade y orphanRemoval
			ordenDao.delete(orden);
			
			return ResponseBuilder.buildSuccessResponseObject(ordenEliminada);
		
		}catch(IllegalArgumentException e) {
			log.error("No se puede eliminar Orden de Compra {}: {}",id,e);
			return ResponseBuilder.buildErrorResponseObject(HttpStatus.BAD_REQUEST, "400", e.getMessage());
			
		}catch(Exception e) {
			log.error("Error al Eliminar la Orden de Compra: ",e);
			return ResponseBuilder.buildErrorResponseObject(HttpStatus.INTERNAL_SERVER_ERROR, "500", "Error en el Sistema");
		}
	}
	
	//===================================================================//
	////////////////////////CANCELAR ORDEN/////////////////////////////////
	//===================================================================//
	@Override
	@Transactional
	public ResponseEntity<ResponseRestObject<OrdenCompraDTO>> cancelarOrdenCompra(CancelarOrdenCompraRequest request, Long id){
		
		try {
			///////////////////
			///VALIDACIONES////
			///////////////////
			if(request == null || request.getMotivo() == null || request.getMotivo().isBlank()) {
				return ResponseBuilder.buildErrorResponseObject(HttpStatus.BAD_REQUEST, "400", "El motivo de la cancelacion es obligatorio");
			}
			
			OrdenCompra orden = ordenDao.findById(id).orElse(null);
			
			if(orden == null) {
				return ResponseBuilder.buildErrorResponseObject(HttpStatus.NOT_FOUND, "404", "No se encontro la orden de compra");
			}
			
			if(orden.getEstadoOrden() == EstadoOrdenCompra.CANCELADA) {
				return ResponseBuilder.buildErrorResponseObject(HttpStatus.BAD_REQUEST, "400", "La orden ya esta cancelada");
			}
			
			if (orden.getEstadoOrden() == EstadoOrdenCompra.COTIZACION) {
			    return ResponseBuilder.buildErrorResponseObject(HttpStatus.BAD_REQUEST, "400", "Una cotización debe eliminarse, no cancelarse");
			}
			
			//////////////////
			/////ASIGNAR//////
			//////////////////
						
			orden.setEstadoOrden(EstadoOrdenCompra.CANCELADA);
			orden.setFechaCancelacion(LocalDateTime.now());
			orden.setMotivoCancelacion(request.getMotivo().trim());
			
			//////////////
			///GUARDAR////
			//////////////
			
			OrdenCompra ordenCancelada = ordenDao.save(orden);
			
			return ResponseBuilder.buildSuccessResponseObject(OrdenCompraMapper.toDTO(ordenCancelada));
		}catch(Exception e) {
			log.error("Error al Cancelar la Orden de Compra: ",e);
			return ResponseBuilder.buildErrorResponseObject(HttpStatus.INTERNAL_SERVER_ERROR, "500", "Error en el Sistema");
		}
		
	}
	
	/*******************************************************************************/
	/***********************************HELPERS*************************************/
	/*******************************************************************************/
	
	private void asignarCliente(Long clienteId,OrdenCompra oc) {
		
		Cliente cliente = clienteDao.findById(clienteId)
				.orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con ID: " + clienteId));
		oc.setCliente(cliente);
		
	}
	
	private void asignarDetalleCalcularTotal(OrdenCompra oc) {
		
		if (oc.getDetalles() == null || oc.getDetalles().isEmpty()) {
			oc.setTotal(BigDecimal.ZERO);
	        return;
	    }
		
		BigDecimal total = BigDecimal.ZERO;
		
		for(OrdenDetalle detalle : oc.getDetalles()) {
			
			BigDecimal subtotal = detalle.getPrecioUnitario()
		            .multiply(BigDecimal.valueOf(detalle.getCantidad()));

		    detalle.setPrecioTotal(subtotal);

		    total = total.add(subtotal);
		}
		
		oc.setTotal(total);
		
	}
	
	private void guardarDetalles(List<CrearOrdenDetalleRequest> detallesRequest, OrdenCompra oc) {
		
		//no ocupo guardar porque ya hibernate lo hace por si solo al  decirle que detalles es cascade	
		//obDao.saveAll(detalles);
			
		oc.setDetalles(construirDetalles(detallesRequest, oc));	
		
	}
	
	
	private void reemplazarDetalles(
			List<CrearOrdenDetalleRequest> detallesRequest, OrdenCompra orden) {
		
		detallesRequest.forEach(this::validarDetalle);
		
		List<OrdenDetalle> nuevosDetalles = construirDetalles(detallesRequest,orden);
		
		//Es preferible conservar la misma coleccion gestionada por
	    //Hibernate cuando orphanRemoval está habilitado
		
		orden.getDetalles().clear();
		orden.getDetalles().addAll(nuevosDetalles);
		
	}
	
	private List<OrdenDetalle> construirDetalles(List<CrearOrdenDetalleRequest> detallesRequest, OrdenCompra orden){
		detallesRequest.forEach(this::validarDetalle);
		
		return detallesRequest.stream()
				.map(detalleRequest -> {
					OrdenDetalle detalle = CrearOrdenDetalleMapper.toEntity(detalleRequest);
					
					detalle.setOrden(orden);
					
					detalle.setMueble(
							muebleDao.findById(detalleRequest.getMuebleId())
								.orElseThrow(() -> 
									new IllegalArgumentException("Mueble No encontrado con ID: " + detalleRequest.getMuebleId())
								)
						);
					
					return detalle;
				})
				.collect(Collectors.toList());
	}
	
	/*******************************************************************************/
	/********************************VALIDACIONES***********************************/
	/*******************************************************************************/
	
	
	private void validarRequest(Long clienteId, List<CrearOrdenDetalleRequest> detalles) {
		
		if(clienteId == null) {
			throw new IllegalArgumentException("Debe Seleccionar Cliente");
		}
		
		if(detalles == null || detalles.isEmpty()) {
			throw new IllegalArgumentException("Debe agregar al menos un mueble");
		}
		
	}
	
	private void validarFechaEntregaCrear(LocalDate fechaEntrega) {

	    if (fechaEntrega == null) {
	        throw new IllegalArgumentException( "La fecha de entrega es obligatoria");
	    }

	    if (fechaEntrega.isBefore(LocalDate.now())) {
	    	throw new IllegalArgumentException("La fecha de entrega no puede ser anterior a la fecha actual");
	    }

	}
	
	private void validarFechaEntregaActualizacion(LocalDate nuevaFecha, OrdenCompra orden) {

	    if (nuevaFecha == null) {
	        throw new IllegalArgumentException("La fecha de entrega es obligatoria");
	    }

	    LocalDate fechaActual = orden.getFechaEntrega();

	    boolean fechaModificada = fechaActual == null || !fechaActual.equals(nuevaFecha);

	    if (fechaModificada && nuevaFecha.isBefore(LocalDate.now())) {
	        throw new IllegalArgumentException("La nueva fecha de entrega no puede ser anterior a la fecha actual");
	    }
	    
	}
	
	private void validarDetalle(CrearOrdenDetalleRequest detalle) {
		if(detalle == null) {
			throw new IllegalArgumentException("Detalle no puede ser nulo");
		}
		
		if(detalle.getMuebleId() == null) {
			throw new IllegalArgumentException("Debe Seleccionar un Mueble");
		}
		
		if(detalle.getCantidad() == null || detalle.getCantidad() <= 0) {
			throw new IllegalArgumentException("Cantidad Invalida");
		}
		
		if (detalle.getPrecioUnitario() == null
	            || detalle.getPrecioUnitario().compareTo(BigDecimal.ZERO) <= 0) {

	        throw new IllegalArgumentException("Precio invalido.");
	    }
	}
	
	private void validarOrdenEditable(OrdenCompra orden) {
		
		if(orden.getEstadoOrden() == EstadoOrdenCompra.CANCELADA) {
			throw new IllegalArgumentException("No se puede editar una orden cancelada");
		}
		
		if(orden.getEstadoOrden() == EstadoOrdenCompra.FINALIZADA) {
			throw new IllegalArgumentException("No se puede editar una orden finalizada");
		}
		
	}
	
	private void validarOrdenEliminable(OrdenCompra orden) {
		if(orden.getEstadoOrden() != EstadoOrdenCompra.COTIZACION) {
			throw new IllegalArgumentException("Solo se pueden eliminar Ordenes en estado de COTIZACION");
		}
	}
}
