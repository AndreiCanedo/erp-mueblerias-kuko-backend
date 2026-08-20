package com.bolsadeideas.backend.muebleria.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bolsadeideas.backend.muebleria.model.Muebleria;

@Repository
public interface IMuebleriaDao extends JpaRepository<Muebleria, Long>{

	Optional<Muebleria> findById(Long id);
}
