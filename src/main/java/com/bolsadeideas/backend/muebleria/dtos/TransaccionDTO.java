package com.bolsadeideas.backend.muebleria.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.bolsadeideas.backend.muebleria.model.NaturalezaFinanciera;
import com.bolsadeideas.backend.muebleria.model.TipoReferencia;
import com.bolsadeideas.backend.muebleria.model.TipoTransaccion;

import lombok.Data;

@Data
public class TransaccionDTO {

	private Long id;
    private BigDecimal monto;
    private TipoTransaccion tipo;
    private BigDecimal netoNuevo;
    private BigDecimal netoAnterior;
    private String descripcion;
    private TipoReferencia referenciaTipo;
    private Long referenciaId;
    private String operacionId;
    private LocalDateTime fecha;
    private NaturalezaFinanciera naturaleza;
}
