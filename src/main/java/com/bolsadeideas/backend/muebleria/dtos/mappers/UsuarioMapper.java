package com.bolsadeideas.backend.muebleria.dtos.mappers;


import java.util.List;

import com.bolsadeideas.backend.muebleria.dtos.UsuarioDTO;
import com.bolsadeideas.backend.muebleria.user.Usuarios;

public class UsuarioMapper {
	
	private UsuarioMapper() {}

    public static UsuarioDTO toDTO(Usuarios usuario) {

        if (usuario == null) {
            return null;
        }

        return UsuarioDTO.builder()
                .id(usuario.getId())
                .username(usuario.getUsername())
                .firstName(usuario.getFirstName())
                .lastName(usuario.getLastName())
                .country(usuario.getCountry())
                .role(usuario.getRole())
                .activo(usuario.getActivo())
                .build();
    }

    public static List<UsuarioDTO> toDTOList(List<Usuarios> usuarios) {

        return usuarios.stream()
                .map(UsuarioMapper::toDTO)
                .toList();
    }
}
