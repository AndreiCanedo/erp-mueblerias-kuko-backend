package com.bolsadeideas.backend.muebleria.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "egresos")
public class Egreso{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(length = 150)
	private String nombre;
	
	@Column(nullable = false, length = 250)
	private String motivo;
	
	@Column(length = 500)
	private String justificacion;
	
	@Column(name = "efectivo_entregado", precision = 12, scale = 2)
	private BigDecimal efectivoEntregado;
	
	@Column(nullable = false, precision = 12, scale = 2)
	private BigDecimal monto;
	
	@Column(precision = 12, scale = 2)
	private BigDecimal cambio;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private EstadoEgreso estado = EstadoEgreso.APLICADO;

	@Column(name = "fecha_cancelacion")
	private LocalDateTime fechaCancelacion;

	@Column(name = "motivo_cancelacion", length = 500)
	private String motivoCancelacion;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "forma_pago", nullable = false, length = 30)
	private FormaPago formaPago;
	
	@CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime fecha;
	
	
}
