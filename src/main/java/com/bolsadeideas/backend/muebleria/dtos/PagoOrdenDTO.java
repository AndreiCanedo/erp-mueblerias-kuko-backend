package com.bolsadeideas.backend.muebleria.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.bolsadeideas.backend.muebleria.model.EstadoPagoOrden;
import com.bolsadeideas.backend.muebleria.model.FormaPago;
import com.bolsadeideas.backend.muebleria.model.TipoPagoOrden;

import lombok.Data;

@Data
public class PagoOrdenDTO {
	
	private Long id;
	private Long ordenId;
	private BigDecimal monto;
	private FormaPago formaPago;
	private TipoPagoOrden tipoPago;
    private EstadoPagoOrden estado;
    private String referencia;
    private String observaciones;
    private LocalDateTime fechaRegistro;
    private LocalDateTime fechaCancelacion;
    private String motivoCancelacion;
	
}
