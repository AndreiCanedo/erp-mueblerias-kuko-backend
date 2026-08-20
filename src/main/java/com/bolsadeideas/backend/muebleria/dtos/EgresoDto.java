package com.bolsadeideas.backend.muebleria.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.bolsadeideas.backend.muebleria.model.EstadoEgreso;
import com.bolsadeideas.backend.muebleria.model.FormaPago;

import lombok.Data;

@Data
public class EgresoDto {

	private Long id;
    private String nombre;
    private String motivo;
    private String justificacion;
    private BigDecimal efectivoEntregado;
    private BigDecimal monto;
    private BigDecimal cambio;
    private FormaPago formaPago;
    private EstadoEgreso estado;
    private LocalDateTime fecha;
    
    private LocalDateTime fechaCancelacion;
    private String MotivoCancelacion;
}
