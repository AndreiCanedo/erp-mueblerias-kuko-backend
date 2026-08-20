package com.bolsadeideas.backend.muebleria.dao.request;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class ActualizarOrdenCompraRequest {
	
	private Long clienteId;
	private List<CrearOrdenDetalleRequest> detallesRequest;
	private LocalDate fechaEntrega;
	
}
