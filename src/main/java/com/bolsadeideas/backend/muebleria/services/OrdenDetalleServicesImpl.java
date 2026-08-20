package com.bolsadeideas.backend.muebleria.services;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.backend.muebleria.dao.IMuebleDao;
import com.bolsadeideas.backend.muebleria.dao.IOrdenCompraDao;
import com.bolsadeideas.backend.muebleria.dao.IOrdenDetalleDao;
import com.bolsadeideas.backend.muebleria.dtos.OrdenDetalleDTO;
import com.bolsadeideas.backend.muebleria.dtos.mappers.OrdenDetalleMapper;
import com.bolsadeideas.backend.muebleria.model.Mueble;
import com.bolsadeideas.backend.muebleria.model.OrdenCompra;
import com.bolsadeideas.backend.muebleria.model.OrdenDetalle;
import com.bolsadeideas.backend.muebleria.response.ResponseBuilder;
import com.bolsadeideas.backend.muebleria.response.ResponseRest;
import com.bolsadeideas.backend.muebleria.response.ResponseRestObject;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrdenDetalleServicesImpl implements IOrdenDetalleServices{
	
	@Autowired
	private IOrdenDetalleDao odDao;
	
	@Autowired
	private IOrdenCompraDao ordenDao;
	
	@Autowired
	private IMuebleDao muebleDao;
	
	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<ResponseRest<OrdenDetalleDTO>> searchOrdenDetalle() {
		
		try {
				
			return ResponseBuilder.buildSuccessResponse(
					OrdenDetalleMapper.toDTOList(odDao.findAll())
					);
			
		}catch(Exception e) {
			log.error("Error al consultar Detalles de la Orden de Compra: ",e);
			return ResponseBuilder.buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "500", "Error en el Sistema");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<ResponseRestObject<OrdenDetalleDTO>> searchOrdenDetalleById(Long id) {
		try {
			return odDao.findById(id)
					.map(ordenDetalle -> ResponseBuilder.buildSuccessResponseObject(OrdenDetalleMapper.toDTO(ordenDetalle)))
					.orElseGet(() -> ResponseBuilder.buildErrorResponseObject(HttpStatus.NOT_FOUND, "404", "Error al encontrar detalle de la orden de compra"));
		}catch(Exception e) {
			log.error("Error al consultar Detalles de la Orden de Compra: ",e);
			return ResponseBuilder.buildErrorResponseObject(HttpStatus.INTERNAL_SERVER_ERROR, "500", "Error en el Sistema");
		}
	}

	@Override
	@Transactional
	public ResponseEntity<ResponseRestObject<OrdenDetalleDTO>> saveOrdenDetalle(OrdenDetalleDTO ordenDetalleDTO) {
		
		try {
			//Validaciones Basicas
			if(ordenDetalleDTO.getCantidad() == null || ordenDetalleDTO.getCantidad() == 0) {
				return ResponseBuilder.buildErrorResponseObject(HttpStatus.BAD_REQUEST, "400", "Cantidad debe ser mayor a 0");
			}
			if(ordenDetalleDTO.getPrecioUnitario() == null || ordenDetalleDTO.getPrecioUnitario().compareTo(BigDecimal.ZERO) == 0) {
				return ResponseBuilder.buildErrorResponseObject(HttpStatus.BAD_REQUEST, "400", "Precio Unitario debe ser mayor a 0");
			}
			
			OrdenDetalle ordenDetalle = OrdenDetalleMapper.toEntity(ordenDetalleDTO);
			
			//Asignar Orden
			OrdenCompra orden = ordenDao.findById(ordenDetalleDTO.getOrdenID())
					.orElseThrow(() -> new RuntimeException("Orden no encontrada"));
			ordenDetalle.setOrden(orden);
			
			//Asignar Mueble
			Mueble mueble = muebleDao.findById(ordenDetalleDTO.getMuebleID())
					.orElseThrow(() -> new RuntimeException("Mueble no encontrada"));
			ordenDetalle.setMueble(mueble);
			
			//Save OrdenDetalle
			OrdenDetalle ordenDetalleSave = odDao.save(ordenDetalle);
			
			return ResponseBuilder.buildSuccessResponseObject(OrdenDetalleMapper.toDTO(ordenDetalleSave));
			
		}catch(Exception e) {
			log.error("Error al Guardar Detalles de la Orden de Compra: ",e);
			return ResponseBuilder.buildErrorResponseObject(HttpStatus.INTERNAL_SERVER_ERROR, "500", "Error en el Sistema");
		}
		
	}

	@Override
	@Transactional
	public ResponseEntity<ResponseRestObject<OrdenDetalleDTO>> updateOrdenDetalle(OrdenDetalleDTO ordenDetalleDTO, Long id) {
		try {
			//Validaciones Basicas
			if(ordenDetalleDTO.getCantidad() == null || ordenDetalleDTO.getCantidad() == 0) {
				return ResponseBuilder.buildErrorResponseObject(HttpStatus.BAD_REQUEST, "400", "Cantidad debe ser mayor a 0");
			}
			if(ordenDetalleDTO.getPrecioUnitario() == null || ordenDetalleDTO.getPrecioUnitario().compareTo(BigDecimal.ZERO) == 0) {
				return ResponseBuilder.buildErrorResponseObject(HttpStatus.BAD_REQUEST, "400", "Precio Unitario debe ser mayor a 0");
			}
			
			return odDao.findById(id)
					.map(ordenDetalle -> {
						UpdateODoldToODnew(ordenDetalle,ordenDetalleDTO);
						OrdenDetalle ordenDetalleSave = odDao.save(ordenDetalle);
						return ResponseBuilder.buildSuccessResponseObject(OrdenDetalleMapper.toDTO(ordenDetalleSave));
					})
					.orElseGet(() -> ResponseBuilder.buildErrorResponseObject(HttpStatus.NOT_FOUND, "404", "Error al encontrar detalle de la orden de compra"));
			
		}catch(Exception e) {
			log.error("Error al Actulizar Detalles de la Orden de Compra: ",e);
			return ResponseBuilder.buildErrorResponseObject(HttpStatus.INTERNAL_SERVER_ERROR, "500", "Error en el Sistema");
		}
	}

	@Override
	@Transactional
	public ResponseEntity<ResponseRestObject<OrdenDetalleDTO>> deleteOrdenDetalleById(Long id) {
		try {
			
			return odDao.findById(id)
					.map(ordenDetalle -> {
						odDao.deleteById(id);
						return ResponseBuilder.buildSuccessResponseObject(OrdenDetalleMapper.toDTO(ordenDetalle));
					})
					.orElseGet(() -> ResponseBuilder.buildErrorResponseObject(HttpStatus.NOT_FOUND, "404", "Error al encontrar detalle de la orden de compra"));
		}catch(Exception e) {
			log.error("Error al Eliminar Detalles de la Orden de Compra: ",e);
			return ResponseBuilder.buildErrorResponseObject(HttpStatus.INTERNAL_SERVER_ERROR, "500", "Error en el Sistema");
		}
	}
	
	public void UpdateODoldToODnew(OrdenDetalle oDold, OrdenDetalleDTO oDnew) {
		oDold.setCantidad(oDnew.getCantidad());
		oDold.setPrecioUnitario(oDnew.getPrecioUnitario());
		
	}

}
