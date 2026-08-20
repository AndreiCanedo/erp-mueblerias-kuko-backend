package com.bolsadeideas.backend.muebleria.dtos.mappers;

import java.util.List;
import java.util.stream.Collectors;

import com.bolsadeideas.backend.muebleria.dtos.MuebleDTO;
import com.bolsadeideas.backend.muebleria.model.Mueble;

//@Component
public class MuebleMapper {
	
	public static MuebleDTO toDTO(Mueble m) {
		if(m == null) return null;
		
		MuebleDTO dto = new MuebleDTO();
		dto.setId(m.getId());
		dto.setDescripcion(m.getDescripcion());
		dto.setPrecioReferencia(m.getPrecioReferencia());
		dto.setActivo(m.getActivo());
		if (m.getDisenoMueble() != null) {
	        dto.setDisenoMuebleId(m.getDisenoMueble().getId());
	        dto.setDisenoMuebleNombre(m.getDisenoMueble().getNombre());
	        dto.setDisenoMiniaturaUrl(m.getDisenoMueble().getMiniaturaUrl());
	    }
		
		return dto;
	}
	
	public static List<MuebleDTO> toDTOList(List<Mueble> ms){
		return ms.stream().map(MuebleMapper::toDTO).collect(Collectors.toList());
	}
	
	public static Mueble toEntity(MuebleDTO dto) {
		if(dto == null) return null;
		
		Mueble mueble = new Mueble();
		mueble.setId(dto.getId());
		mueble.setDescripcion(dto.getDescripcion());
		mueble.setPrecioReferencia(dto.getPrecioReferencia());
		
		return mueble;
		
	}
	
}
