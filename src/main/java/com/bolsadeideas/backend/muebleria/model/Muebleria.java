package com.bolsadeideas.backend.muebleria.model;


import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Data;

@Data
@Entity
@Table(name = "mueblerias")
public class Muebleria{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private BigDecimal neto;//Saldo Actual
	
	private BigDecimal totalIngresos;
	private BigDecimal totalEgresos;
	
	
	//Para que si entran dos rquest al mismo tiempo haga una y luego la otra
	@Version
    @Column(nullable = false)
    private Long version;
	
}
