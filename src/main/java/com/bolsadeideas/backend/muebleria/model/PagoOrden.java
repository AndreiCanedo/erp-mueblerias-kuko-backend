package com.bolsadeideas.backend.muebleria.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "pagos_orden")
public class PagoOrden {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch= FetchType.LAZY, optional = false)
	@JoinColumn(name = "orden_id", nullable = false)
	private OrdenCompra orden;
	
	@Column(nullable=false, precision = 12, scale = 2)
	private BigDecimal monto;
	
	@Enumerated(EnumType.STRING)
    @Column(name = "forma_pago", nullable = false, length = 30)
	private FormaPago formaPago;
	
	@Enumerated(EnumType.STRING)
    @Column(name = "tipo_pago", nullable = false, length = 30)
	private TipoPagoOrden tipoPago;
	
	@Enumerated(EnumType.STRING)
    @Column( nullable = false, length = 30)
	private EstadoPagoOrden estado;
	
	@Column(length = 100)
	private String referencia;
	
	@Column(length = 500)
	private String observaciones;
	
	
	@CreationTimestamp
	@Column(name = "fecha_registro", nullable = false, updatable = false)
	private LocalDateTime fechaRegistro;
	
	@Column(name = "fecha_cancelacion")
    private LocalDateTime fechaCancelacion;
	
	@Column(name = "motivo_cancelacion", length = 500)
	private String motivoCancelacion;
	
}
