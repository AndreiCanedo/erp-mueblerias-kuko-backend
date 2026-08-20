package com.bolsadeideas.backend.muebleria.dtos.mappers;

import java.util.List;

import org.springframework.stereotype.Component;

import com.bolsadeideas.backend.muebleria.dtos.TransaccionDTO;
import com.bolsadeideas.backend.muebleria.model.Transaccion;

@Component
public class TransaccionMapper {
	
	public static TransaccionDTO toDTO(Transaccion t) {
        if (t == null) return null;

        TransaccionDTO dto = new TransaccionDTO();
        dto.setId(t.getId());
        dto.setMonto(t.getMonto());
        dto.setNetoAnterior(t.getNetoAnterior());
        dto.setNetoNuevo(t.getNetoNuevo());
        dto.setTipo(t.getTipo());
        dto.setDescripcion(t.getDescripcion());
        dto.setReferenciaTipo(t.getReferenciaTipo());
        dto.setReferenciaId(t.getReferenciaId());
        dto.setOperacionId(t.getOperacionId());
        dto.setNaturaleza(t.getNaturaleza());
        dto.setFecha(t.getFecha());

        return dto;
    }

    public static List<TransaccionDTO> toDTOList(List<Transaccion> list) {
        return list.stream()
                .map(TransaccionMapper::toDTO)
                .toList();
    }
}
