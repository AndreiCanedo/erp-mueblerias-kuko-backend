package com.bolsadeideas.backend.muebleria.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bolsadeideas.backend.muebleria.user.Usuarios;

@Repository
public interface IUserRepository extends JpaRepository<Usuarios, Long>{

	Optional<Usuarios> findByUsername(String username);

    boolean existsByUsernameIgnoreCase(String username);

    boolean existsByUsernameIgnoreCaseAndIdNot(String username, Long id);

    List<Usuarios> findAllByOrderByFirstNameAsc();
	
}
