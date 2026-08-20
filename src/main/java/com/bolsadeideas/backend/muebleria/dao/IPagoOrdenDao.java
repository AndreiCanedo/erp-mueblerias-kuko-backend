package com.bolsadeideas.backend.muebleria.dao;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bolsadeideas.backend.muebleria.model.EstadoPagoOrden;
import com.bolsadeideas.backend.muebleria.model.PagoOrden;

@Repository
public interface IPagoOrdenDao extends JpaRepository<PagoOrden, Long>{
	
	List<PagoOrden> findAllByOrderByFechaRegistroDesc();
	
	List<PagoOrden> findByOrdenIdOrderByFechaRegistroAsc(Long id);
	
	List<PagoOrden> findByOrdenIdAndEstadoOrderByFechaRegistroAsc(Long ordenId, EstadoPagoOrden estado);
	
	boolean existsByOrdenIdAndEstado(Long ordenId, EstadoPagoOrden estado);
	
	@Query("SELECT COALESCE(SUM(p.monto), 0) FROM PagoOrden p WHERE p.orden.id = :ordenId AND p.estado = :estado")
    BigDecimal sumMontoByOrdenIdAndEstado(@Param("ordenId") Long ordenId, @Param("estado") EstadoPagoOrden estado);
	
	@Query("SELECT COALESCE(SUM(p.monto), 0) FROM PagoOrden p WHERE p.estado = :estado")
	BigDecimal sumMontoByEstado(@Param("estado") EstadoPagoOrden estado);
	
	Optional<PagoOrden> findFirstByOrdenIdAndEstadoOrderByFechaRegistroAsc(Long ordenId, EstadoPagoOrden estado);
	
	@Query("SELECT COALESCE(SUM(p.monto), 0) FROM PagoOrden p WHERE p.estado = :estado AND p.fechaRegistro BETWEEN :inicio AND :fin")
	BigDecimal sumMontoByEstadoAndFechaRegistroBetween( @Param("estado") EstadoPagoOrden estado, @Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);

}
