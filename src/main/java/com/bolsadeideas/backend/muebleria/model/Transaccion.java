package com.bolsadeideas.backend.muebleria.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.Data;

@Data
@Entity
public class Transaccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal monto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoTransaccion tipo; // INGRESO / EGRESO / AJUSTE
    
    @Column(name = "neto_anterior", nullable = false)
    private BigDecimal netoAnterior;
    
    @Column(name = "neto_nuevo", nullable = false)
    private BigDecimal netoNuevo;

    @Column(nullable = false, length = 255)
    private String descripcion;

    // REFERENCIA GENÉRICA (ID de ingreso, egreso, orden, etc.)
    @Column(name = "referencia_id")
    private Long referenciaId;

    @Column(name = "referencia_tipo", length = 50)
    @Enumerated(EnumType.STRING)
    private TipoReferencia referenciaTipo;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fecha;
    
    @Column(name = "operacion_id",unique = true, nullable = false, updatable = false, length = 36)
    private String operacionId;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private NaturalezaFinanciera naturaleza;
    
    @PrePersist
    public void prePersist() {
    	if(fecha == null) {
    		this.fecha = LocalDateTime.now();    		
    	}
    }
	
}
