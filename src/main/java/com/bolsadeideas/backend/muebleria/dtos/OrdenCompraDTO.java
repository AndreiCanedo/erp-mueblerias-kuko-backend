package com.bolsadeideas.backend.muebleria.dtos;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.bolsadeideas.backend.muebleria.model.EstadoEntrega;
import com.bolsadeideas.backend.muebleria.model.EstadoOrdenCompra;
import com.bolsadeideas.backend.muebleria.model.EstadoPago;
import com.bolsadeideas.backend.muebleria.model.Proceso;

import lombok.Data;

@Data
public class OrdenCompraDTO {
	
	private Long id;
	private Long clienteId;
	private LocalDateTime fecha;
	private BigDecimal total;
	private List<OrdenDetalleDTO> detalles;
	private LocalDateTime fechaConfirmacion;
	private LocalDate fechaEntrega;
	private LocalDateTime fechaCancelacion;
	private String motivoCancelacion;
	private EstadoOrdenCompra estadoOrden;
	private Proceso proceso;
	private EstadoPago estadoPago;
	private EstadoEntrega estadoEntrega;
	
	
}
