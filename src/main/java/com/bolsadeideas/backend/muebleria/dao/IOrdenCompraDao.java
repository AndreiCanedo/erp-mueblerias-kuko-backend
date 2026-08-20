package com.bolsadeideas.backend.muebleria.dao;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bolsadeideas.backend.muebleria.model.EstadoEntrega;
import com.bolsadeideas.backend.muebleria.model.EstadoOrdenCompra;
import com.bolsadeideas.backend.muebleria.model.OrdenCompra;
import com.bolsadeideas.backend.muebleria.model.Proceso;

@Repository
public interface IOrdenCompraDao extends JpaRepository<OrdenCompra, Long>{
	
	List<OrdenCompra> findByEstadoOrdenIn( Collection<EstadoOrdenCompra> estados);

    long countByEstadoOrdenIn( Collection<EstadoOrdenCompra> estados);

    long countByProceso(Proceso proceso);

    long countByEstadoEntrega(EstadoEntrega estadoEntrega);

    long countByFechaEntregaBeforeAndEstadoEntregaNot(LocalDateTime fecha, EstadoEntrega estadoEntrega);

    List<OrdenCompra> findTop10ByFechaEntregaAfterAndEstadoEntregaNotOrderByFechaEntregaAsc(LocalDateTime fecha, EstadoEntrega estadoEntrega);

}
