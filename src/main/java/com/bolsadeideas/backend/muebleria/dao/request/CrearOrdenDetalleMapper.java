package com.bolsadeideas.backend.muebleria.dao.request;

import java.math.BigDecimal;

import com.bolsadeideas.backend.muebleria.model.OrdenDetalle;

public class CrearOrdenDetalleMapper {
	
	public static OrdenDetalle toEntity(CrearOrdenDetalleRequest request){
		
		if(request == null) return null;
		
		OrdenDetalle detalle = new OrdenDetalle();
		
		detalle.setCantidad(request.getCantidad());
		detalle.setPrecioUnitario(request.getPrecioUnitario());
		
		BigDecimal total = request.getPrecioUnitario().multiply(BigDecimal.valueOf(request.getCantidad()));
		
		detalle.setPrecioTotal(total);
		
		return detalle;
		
	}

}
