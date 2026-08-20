package com.bolsadeideas.backend.muebleria.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "muebles")
public class Mueble {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String descripcion;
	
	private BigDecimal precioReferencia;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diseno_mueble_id", nullable = true)
    private DisenoMueble disenoMueble;

    @Column(nullable = false)
    private Boolean activo = true;
	
}
