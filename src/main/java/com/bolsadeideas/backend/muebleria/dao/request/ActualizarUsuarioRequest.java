package com.bolsadeideas.backend.muebleria.dao.request;

import com.bolsadeideas.backend.muebleria.user.Role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActualizarUsuarioRequest {
	
	@NotBlank(message = "El usuario es obligatorio")
    private String username;

    private String firstName;

    private String lastName;

    private String country;

    @NotNull(message = "El rol es obligatorio")
    private Role role;

    @NotNull(message = "El estado del usuario es obligatorio")
    private Boolean activo;
	
}
