package com.bolsadeideas.backend.muebleria.dtos.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.bolsadeideas.backend.muebleria.dtos.OrdenDetalleDTO;
import com.bolsadeideas.backend.muebleria.model.OrdenDetalle;

@Component
public class OrdenDetalleMapper {

	
	public static OrdenDetalleDTO toDTO(OrdenDetalle od) {
		
		if(od == null) return null;
		
		OrdenDetalleDTO dto = new OrdenDetalleDTO();
		
		dto.setId(od.getId());
		dto.setCantidad(od.getCantidad());
		dto.setPrecioUnitario(od.getPrecioUnitario());
		dto.setPrecioTotal(od.getPrecioTotal());
		dto.setOrdenID(
				od.getOrden() != null ? od.getOrden().getId() : null
						);
		dto.setMuebleID(
				od.getMueble() != null ? od.getMueble().getId() : null
				);
		
		return dto;
	}
	
	public static List<OrdenDetalleDTO> toDTOList(List<OrdenDetalle> ods){
		return ods.stream().map(OrdenDetalleMapper :: toDTO).collect(Collectors.toList());
	}
	
	public static OrdenDetalle toEntity(OrdenDetalleDTO dto) {
		
		if(dto == null) return null;
		
		OrdenDetalle od = new OrdenDetalle();
		
		od.setId(dto.getId());
		od.setCantidad(dto.getCantidad());
		od.setPrecioUnitario(dto.getPrecioUnitario());
		od.setPrecioTotal(dto.getPrecioTotal());
		
		return od;
		
	}
}
