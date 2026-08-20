package com.bolsadeideas.backend.muebleria.dtos.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.bolsadeideas.backend.muebleria.dtos.OrdenCompraDTO;
import com.bolsadeideas.backend.muebleria.model.OrdenCompra;

@Component
public class OrdenCompraMapper {

	public static OrdenCompraDTO toDTO(OrdenCompra oc) {
		
		if(oc == null) return null;
		
		OrdenCompraDTO dto = new OrdenCompraDTO();
		dto.setId(oc.getId());
		dto.setClienteId(
				oc.getCliente() != null ? oc.getCliente().getId() : null
				);
		dto.setFecha(oc.getFecha());
		dto.setTotal(oc.getTotal());
		dto.setDetalles(
				oc.getDetalles() != null
				? oc.getDetalles().stream().map(OrdenDetalleMapper::toDTO).collect(Collectors.toList()) : null
				);
		dto.setEstadoOrden(oc.getEstadoOrden());
		dto.setFechaConfirmacion(oc.getFechaConfirmacion());
		dto.setFechaEntrega(oc.getFechaEntrega());
		dto.setFechaCancelacion(oc.getFechaCancelacion());
		dto.setMotivoCancelacion(oc.getMotivoCancelacion());
		dto.setProceso(oc.getProceso());
		dto.setEstadoPago(oc.getEstadoPago());
		dto.setEstadoEntrega(oc.getEstadoEntrega());
		
		return dto;
	}
	
	public static List<OrdenCompraDTO> toDTOList(List<OrdenCompra> ocs){
		return ocs.stream().map(OrdenCompraMapper :: toDTO).collect(Collectors.toList());
	}
	
	public static OrdenCompra toEntity(OrdenCompraDTO dto) {
		
		if(dto == null) return null;
		
		OrdenCompra oc = new OrdenCompra();
		oc.setId(dto.getId());
		oc.setTotal(dto.getTotal());
		oc.setFechaEntrega(dto.getFechaEntrega());
		oc.setProceso(dto.getProceso());
		oc.setEstadoOrden(dto.getEstadoOrden());
		oc.setEstadoPago(dto.getEstadoPago());
		oc.setEstadoEntrega(dto.getEstadoEntrega());
		
		return oc;
	}
	
}
