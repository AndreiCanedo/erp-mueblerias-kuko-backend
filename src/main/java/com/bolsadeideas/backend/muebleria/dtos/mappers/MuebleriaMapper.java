package com.bolsadeideas.backend.muebleria.dtos.mappers;

import org.springframework.stereotype.Component;

import com.bolsadeideas.backend.muebleria.dtos.MuebleriaDTO;
import com.bolsadeideas.backend.muebleria.model.Muebleria;

@Component
public class MuebleriaMapper {
	
	public static MuebleriaDTO toDTO(Muebleria m) {
        if (m == null) return null;

        MuebleriaDTO dto = new MuebleriaDTO();
        dto.setId(m.getId());
        dto.setNeto(m.getNeto());
        dto.setTotalIngresos(m.getTotalIngresos());
        dto.setTotalEgresos(m.getTotalEgresos());

        return dto;
    }

}
