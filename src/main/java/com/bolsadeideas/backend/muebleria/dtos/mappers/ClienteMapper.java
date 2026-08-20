package com.bolsadeideas.backend.muebleria.dtos.mappers;

import java.util.List;
import java.util.stream.Collectors;

import com.bolsadeideas.backend.muebleria.dtos.ClienteDTO;
import com.bolsadeideas.backend.muebleria.model.Cliente;

//@Component
public class ClienteMapper {
	
	public static ClienteDTO toDTO(Cliente c) {
		
		if(c == null) return null;
		
		ClienteDTO dto = new ClienteDTO();
		dto.setId(c.getId());
		dto.setNombre(c.getNombre());
		dto.setDireccion(c.getDireccion());
		dto.setTelefono(c.getTelefono());
		dto.setCorreo(c.getCorreo());
		dto.setRfc(c.getRfc());
		
		return dto;
	}
	
	public static List<ClienteDTO> toDTOList(List<Cliente> cs){
		return 	cs.stream().map(ClienteMapper::toDTO).collect(Collectors.toList());
	}
	
	public static Cliente toEntity(ClienteDTO dto) {
		
		if(dto == null) return null;
		
		Cliente cliente = new Cliente();
		cliente.setNombre(dto.getNombre());
		cliente.setDireccion(dto.getDireccion());
		cliente.setTelefono(dto.getTelefono());
		cliente.setCorreo(dto.getCorreo());
		cliente.setRfc(dto.getRfc());
		
		return cliente;
		
		
	}
	
}
