package com.bolsadeideas.backend.muebleria.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bolsadeideas.backend.muebleria.model.CategoriaMueble;
import com.bolsadeideas.backend.muebleria.model.DisenoMueble;

@Repository
public interface IDisenoMuebleDao extends JpaRepository<DisenoMueble, Long>{
	
	boolean existsByNombreIgnoreCase(String nombre);
	boolean existsByNombreIgnoreCaseAndIdNot(String nombre, Long id);
	
	List<DisenoMueble> findByActivoTrueOrderByFechaRegistroDesc();
	
	List<DisenoMueble> findByCategoriaAndActivoTrueOrderByFechaRegistroDesc(CategoriaMueble categoria);
	
	@Query("SELECT d FROM DisenoMueble d WHERE d.activo = true AND (LOWER(d.nombre) LIKE LOWER(CONCAT('%', :texto, '%')) OR LOWER(COALESCE(d.descripcion, '')) LIKE LOWER(CONCAT('%', :texto, '%'))) ORDER BY d.fechaRegistro DESC")
	    List<DisenoMueble> buscarActivos(@Param("texto") String texto);
}
