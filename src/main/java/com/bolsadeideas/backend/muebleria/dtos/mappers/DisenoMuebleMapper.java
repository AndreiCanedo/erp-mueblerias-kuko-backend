package com.bolsadeideas.backend.muebleria.dtos.mappers;

import java.util.List;

import com.bolsadeideas.backend.muebleria.dao.request.CrearDisenoMuebleRequest;
import com.bolsadeideas.backend.muebleria.dtos.DisenoMuebleDTO;
import com.bolsadeideas.backend.muebleria.model.DisenoMueble;

public class DisenoMuebleMapper {
	
	private DisenoMuebleMapper() {
    }

    public static DisenoMuebleDTO toDTO(DisenoMueble diseno) {

        DisenoMuebleDTO dto = new DisenoMuebleDTO();

        dto.setId(diseno.getId());
        dto.setNombre(diseno.getNombre());
        dto.setDescripcion(diseno.getDescripcion());
        dto.setCategoria(diseno.getCategoria());
        dto.setImagenUrl(diseno.getImagenUrl());
        dto.setMiniaturaUrl(diseno.getMiniaturaUrl());
        dto.setFechaRegistro(diseno.getFechaRegistro());
        dto.setActivo(diseno.getActivo());

        return dto;
    }

    public static List<DisenoMuebleDTO> toDTOList(List<DisenoMueble> disenos) {

        return disenos.stream().map(DisenoMuebleMapper::toDTO).toList();
    }

    public static DisenoMueble fromCreateRequest(CrearDisenoMuebleRequest request) {

        DisenoMueble diseno = new DisenoMueble();

        diseno.setNombre(request.getNombre());
        diseno.setDescripcion(request.getDescripcion());
        diseno.setCategoria(request.getCategoria());
        diseno.setImagenUrl(request.getImagenUrl());
        diseno.setMiniaturaUrl(request.getMiniaturaUrl());
        diseno.setActivo(true);

        return diseno;
    }

	
}
