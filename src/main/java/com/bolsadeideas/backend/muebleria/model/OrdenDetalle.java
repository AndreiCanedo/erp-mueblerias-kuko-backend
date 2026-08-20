package com.bolsadeideas.backend.muebleria.model;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "orden_detalle")
public class OrdenDetalle {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Double cantidad;
	
	private BigDecimal precioUnitario;
	
	private BigDecimal precioTotal;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "orden_id", nullable = false)
	private OrdenCompra orden;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "mueble_id", nullable = false)
	private Mueble mueble;
	
	
	@PrePersist
	@PreUpdate
	public void calcularTotal() {
		if (this.cantidad != null && this.precioUnitario != null) {
	        this.precioTotal = BigDecimal.valueOf(this.cantidad)
	                .multiply(this.precioUnitario);
	    }
	}
}
