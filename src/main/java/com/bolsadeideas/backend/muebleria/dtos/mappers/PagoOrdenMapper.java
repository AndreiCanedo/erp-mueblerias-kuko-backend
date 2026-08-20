package com.bolsadeideas.backend.muebleria.dtos.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.bolsadeideas.backend.muebleria.dtos.PagoOrdenDTO;
import com.bolsadeideas.backend.muebleria.model.PagoOrden;

@Component
public class PagoOrdenMapper {
	
	public static PagoOrdenDTO toDTO(PagoOrden pago) {
		
		if(pago == null) return null;
		
		PagoOrdenDTO dto = new PagoOrdenDTO();
		
		dto.setId(pago.getId());
		dto.setOrdenId(
				pago.getOrden() != null ? pago.getOrden().getId() : null
				);
		dto.setMonto(pago.getMonto());
		dto.setFormaPago(pago.getFormaPago());
		dto.setTipoPago(pago.getTipoPago());
		dto.setEstado(pago.getEstado());
		dto.setReferencia(pago.getReferencia());
		dto.setObservaciones(pago.getObservaciones());
		dto.setFechaRegistro(pago.getFechaRegistro());
		dto.setFechaCancelacion(pago.getFechaCancelacion());
		dto.setMotivoCancelacion(pago.getMotivoCancelacion());

		
		return dto;
	}
	
	public static List<PagoOrdenDTO> toDTOList(List<PagoOrden> pagos){
		return pagos.stream().map(PagoOrdenMapper :: toDTO).collect(Collectors.toList());
	}
	
	//No crearems to entity porque la creacion tendra reglas  de negocio importantes lo cual se debe hacer en services
	/*public static PagoOrden toEntity(PagoOrdenDTO dto) {
		
		if(dto == null) return null;
		
		PagoOrden ig = new PagoOrden();
		
		ig.setId(dto.getId());
		ig.setFormaPago(dto.getFormaPago());
		ig.setMonto(dto.getMonto());
		ig.setFecha(dto.getFecha());
		
		return ig;
	}*/
	
}
