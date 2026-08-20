package com.bolsadeideas.backend.muebleria.exceptions;

public class MuebleriaNotFoundException extends RuntimeException{

	public MuebleriaNotFoundException(Long id) {
		super("Muebleria con ID " + id + " no encontrada");
	}
	
}
