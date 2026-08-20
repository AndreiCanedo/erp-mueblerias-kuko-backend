package com.bolsadeideas.backend.muebleria.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bolsadeideas.backend.muebleria.model.Cliente;

@Repository
public interface IClienteDao extends JpaRepository<Cliente, Long>{

	@Query("""
			SELECT c
			FROM Cliente c
			WHERE
				LOWER(c.nombre) LIKE LOWER(CONCAT('%', :texto, '%'))
				OR c.telefono LIKE CONCAT('%', :texto, '%')
				OR LOWER(c.correo) LIKE LOWER(CONCAT('%', :texto, '%'))
				OR LOWER(c.rfc) LIKE LOWER(CONCAT('%', :texto, '%'))
			ORDER BY c.nombre
			""")
	List<Cliente> buscarClientes(@Param("texto") String texto);
	
	boolean existsByRfc(String rfc);
	boolean existsByCorreo(String correo);
	
	boolean existsByCorreoAndIdNot(String correo, Long id);
	boolean existsByRfcAndIdNot(String rfc, Long id);
}
