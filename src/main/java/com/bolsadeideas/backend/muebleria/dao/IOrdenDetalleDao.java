package com.bolsadeideas.backend.muebleria.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bolsadeideas.backend.muebleria.model.OrdenDetalle;

@Repository
public interface IOrdenDetalleDao extends JpaRepository<OrdenDetalle, Long>{
	
	//Forma Manual
	//@Query("SELECT od FROM OrdenDetalle od WHERE od.orden.id = :ordenId")
	//List<OrdenDetalle> findByOrdenId(@Param("ordenId") Long ordenId);

	//Forma Automatica por Sprind Boot
	List<OrdenDetalle> findByOrdenId(Long ordenId);
	
	void deleteByOrdenId(Long ordenId);
}
