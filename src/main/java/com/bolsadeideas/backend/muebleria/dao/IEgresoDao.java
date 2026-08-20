package com.bolsadeideas.backend.muebleria.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bolsadeideas.backend.muebleria.model.Egreso;

@Repository
public interface IEgresoDao extends JpaRepository<Egreso, Long>{
	
	@Query(value = "SELECT * FROM egreso WHERE forma_pago = :formaPago", nativeQuery = true)
	List<Egreso> findByFormaPago(@Param("formaPago") String formaPago);

}
