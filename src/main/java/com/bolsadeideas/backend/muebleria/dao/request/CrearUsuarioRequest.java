package com.bolsadeideas.backend.muebleria.dao.request;

import com.bolsadeideas.backend.muebleria.user.Role;
import com.bolsadeideas.backend.muebleria.validations.ValidPassword;

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
public class CrearUsuarioRequest {
	
	@NotBlank(message = "El usuario es obligatorio")
    private String username;

    @ValidPassword
    private String password;

    @NotBlank(message = "La confirmación de contraseña es obligatoria")
    private String passwordConfirm;

    private String firstName;

    private String lastName;

    private String country;

    @NotNull(message = "El rol es obligatorio")
    private Role role;
	
}
