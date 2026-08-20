package com.bolsadeideas.backend.muebleria.dao.request;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class CrearOrdenCompraRequest {
	
	private Long clienteId;
	
	private LocalDate fechaEntrega;
	
	private List<CrearOrdenDetalleRequest> detallesRequest;
	
}
