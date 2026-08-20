package com.bolsadeideas.backend.muebleria.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bolsadeideas.backend.muebleria.model.TipoReferencia;
import com.bolsadeideas.backend.muebleria.model.TipoTransaccion;
import com.bolsadeideas.backend.muebleria.model.Transaccion;

@Repository
public interface ITransaccionDao extends JpaRepository<Transaccion, Long>{
	
	List<Transaccion> findByTipo(TipoTransaccion tipo);

	List<Transaccion> findByFechaBetween(LocalDateTime inicio, LocalDateTime fin);
	
	List<Transaccion> findByFechaBetweenOrderByFechaAsc(LocalDateTime inicio, LocalDateTime fin);
	
	List<Transaccion> findTop10ByOrderByFechaDesc();
	
	boolean existsByReferenciaIdAndReferenciaTipo(Long referenciaId, TipoReferencia referenciaTipo);
	
	boolean existsByOperacionId(String operacionId);

}
