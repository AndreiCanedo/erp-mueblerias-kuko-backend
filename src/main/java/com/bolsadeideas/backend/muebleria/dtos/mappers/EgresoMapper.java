package com.bolsadeideas.backend.muebleria.dtos.mappers;

import java.util.List;

import org.springframework.stereotype.Component;

import com.bolsadeideas.backend.muebleria.dtos.EgresoDto;
import com.bolsadeideas.backend.muebleria.model.Egreso;

@Component
public class EgresoMapper {
	
	public static EgresoDto toDTO(Egreso e) {
		 
		if (e == null) return null;
		
		EgresoDto dto = new EgresoDto();
		
		dto.setId(e.getId());
        dto.setNombre(e.getNombre());
        dto.setMotivo(e.getMotivo());
        dto.setJustificacion(e.getJustificacion());
        dto.setEfectivoEntregado(e.getEfectivoEntregado());
        dto.setMonto(e.getMonto());
        dto.setCambio(e.getCambio());
        dto.setEstado(e.getEstado());
        dto.setFormaPago(e.getFormaPago());
        dto.setFecha(e.getFecha());
        dto.setFechaCancelacion(e.getFechaCancelacion());
        dto.setMotivoCancelacion(e.getMotivoCancelacion());
        
        return dto;
		
	}
	
	public static List<EgresoDto> toDTOList(List<Egreso> list) {
		return list.stream().map(EgresoMapper::toDTO).toList();
	}
	
	public static Egreso toEntity(EgresoDto dto) {
		
		if(dto == null) return null;
		
		Egreso e = new Egreso();
		
		e.setId(dto.getId());
        e.setNombre(dto.getNombre());
        e.setMotivo(dto.getMotivo());
        e.setJustificacion(dto.getJustificacion());
        e.setMonto(dto.getMonto());
        e.setCambio(dto.getCambio());
        e.setFormaPago(dto.getFormaPago());
        e.setEstado(dto.getEstado());
        e.setFecha(dto.getFecha());
        e.setFechaCancelacion(dto.getFechaCancelacion());
        e.setMotivoCancelacion(dto.getMotivoCancelacion());
        
        return e;
		
		
	}
	
}
