package com.bolsadeideas.backend.muebleria.dtos;

import lombok.Data;

@Data
public class ClienteDTO {
	private Long id;
    private String nombre;
    private String direccion;
    private String telefono;
    private String correo;
    private String rfc;
}
