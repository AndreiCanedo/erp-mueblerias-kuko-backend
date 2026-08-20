package com.bolsadeideas.backend.muebleria.model;

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
@Table(name= "disenos_mueble")
public class DisenoMueble {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 150)
    private String nombre;

    @Column(length = 700)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private CategoriaMueble categoria;

    @Column(name = "imagen_url", nullable = false, length = 500)
    private String imagenUrl;

    @Column(name = "miniatura_url", length = 500)
    private String miniaturaUrl;

    @CreationTimestamp
    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    @Column(nullable = false)
    private Boolean activo = true;
	
	
}
