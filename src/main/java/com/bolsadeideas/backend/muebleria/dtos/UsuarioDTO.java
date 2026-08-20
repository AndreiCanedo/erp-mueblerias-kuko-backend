package com.bolsadeideas.backend.muebleria.dtos;

import com.bolsadeideas.backend.muebleria.user.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
	
	 private Long id;
	 private String username;
	 private String firstName;
	 private String lastName;
	 private String country;
	 private Role role;
	 private Boolean activo;
	
}
