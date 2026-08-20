package com.bolsadeideas.backend.muebleria.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bolsadeideas.backend.muebleria.model.Mueble;

@Repository
public interface IMuebleDao extends JpaRepository<Mueble, Long>{

	@Query("""
			SELECT m
			FROM Mueble m
			WHERE LOWER(m.descripcion)
			LIKE LOWER(CONCAT('%', :texto, '%'))
			ORDER BY m.descripcion
			""")
	List<Mueble> buscarMuebles(String texto);
	
	List<Mueble> findByActivoTrueOrderByDescripcionAsc();
	
	List<Mueble> findByDisenoMuebleIdAndActivoTrueOrderByDescripcionAsc(Long disenoMuebleId);
	
	boolean existsByDescripcionIgnoreCase(String descripcion);

	boolean existsByDescripcionIgnoreCaseAndIdNot(String descripcion, Long id);
	
}
