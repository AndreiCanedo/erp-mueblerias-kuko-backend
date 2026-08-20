package com.bolsadeideas.backend.muebleria.dtos;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class MuebleriaDTO {

	private Long id;
    private BigDecimal neto;
    private BigDecimal totalIngresos;
    private BigDecimal totalEgresos;
}
