package com.bolsadeideas.backend.muebleria.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

	@Data
	@Entity
	@Table(name= "orden_compras")
	public class OrdenCompra {
	
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long id;
		
		@ManyToOne
		private Cliente cliente;
		
		@CreationTimestamp
		@Column(updatable = false)
		private LocalDateTime fecha;
		
		@OneToMany(mappedBy = "orden", fetch= FetchType.LAZY)
		private List<PagoOrden> pagos = new ArrayList<>();
		
		private BigDecimal total;
		
		@OneToMany(mappedBy = "orden", cascade = CascadeType.ALL, orphanRemoval = true)
		private List<OrdenDetalle> detalles = new ArrayList<>();
		
		@Column(name = "fecha_confirmacion")
	    private LocalDateTime fechaConfirmacion;
		
		@Column(name = "fecha_entrega", nullable = false)
		private LocalDate fechaEntrega;
		
		
		@Column(name = "fecha_cancelacion")
		private LocalDateTime fechaCancelacion;

		@Column(name = "motivo_cancelacion", length = 500)
		private String motivoCancelacion;
		
		@Enumerated(EnumType.STRING)
		@Column(nullable = false)
		private EstadoOrdenCompra estadoOrden;
		
		@Enumerated(EnumType.STRING)
		@Column(nullable = false)
		private Proceso proceso;
		
		@Enumerated(EnumType.STRING)
		@Column(nullable = false)
		private EstadoPago estadoPago;
		
		@Enumerated(EnumType.STRING)
		@Column(nullable = false)
		private EstadoEntrega estadoEntrega;
		
	}
 