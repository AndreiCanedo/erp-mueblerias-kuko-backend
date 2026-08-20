package com.bolsadeideas.backend.muebleria.services;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.backend.muebleria.dao.IClienteDao;
import com.bolsadeideas.backend.muebleria.dtos.ClienteDTO;
import com.bolsadeideas.backend.muebleria.dtos.mappers.ClienteMapper;
import com.bolsadeideas.backend.muebleria.model.Cliente;
import com.bolsadeideas.backend.muebleria.response.ResponseBuilder;
import com.bolsadeideas.backend.muebleria.response.ResponseRest;
import com.bolsadeideas.backend.muebleria.response.ResponseRestObject;

import lombok.extern.slf4j.Slf4j;

/**
 * Guarda un nuevo cliente validando nombre y formato de correo.
 * @param cliente Datos del cliente a guardar.
 * @return ResponseEntity con el cliente creado o error.
 */

@Service
@Slf4j
public class ClientesServicesImpl implements IClientesServices{
	
	@Autowired
	private IClienteDao clienteDao;

	//===================================================================//
	////////////////////////BUSCAR CLIENTES////////////////////////////////
	//===================================================================//	
	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<ResponseRest<ClienteDTO>> searchClientes() {
		try {
			return ResponseBuilder.buildSuccessResponse(
					ClienteMapper.toDTOList(clienteDao.findAll())
					);
			
		}catch(Exception e) {
			log.error("Error al consultar los clientes: ",e);
			return ResponseBuilder.buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "500", "Error en el Sistema");
		}
	}

	//===================================================================//
	////////////////////////BUSCAR CLIENTE POR ID//////////////////////////
	//===================================================================//	

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<ResponseRestObject<ClienteDTO>> searchClienteById(Long id) {
		try {
			return clienteDao.findById(id)
					.map(cliente -> ResponseBuilder.buildSuccessResponseObject(ClienteMapper.toDTO(cliente)))
					.orElseGet(() -> ResponseBuilder.buildErrorResponseObject(HttpStatus.NOT_FOUND, "404", "No se Encontro el cliente"));
			
		}catch(Exception e) {
			log.error("Error al consultar los clientes: ",e);
			return ResponseBuilder.buildErrorResponseObject(HttpStatus.INTERNAL_SERVER_ERROR, "500", "Error en el Sistema");
		}
	}
	
	//===================================================================//
	////////////////////////BUSCAR CLIENTES POR TEXTO//////////////////////
	//===================================================================//	
	
	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<ResponseRest<ClienteDTO>> buscarClientes(String texto){
		
		try {
			
		if(texto == null || texto.trim().isEmpty()) {
			return ResponseBuilder.buildSuccessResponse(List.of());
		}
		
		String textoNormalizado = texto.trim();
		
		return ResponseBuilder.buildSuccessResponse(ClienteMapper.toDTOList(clienteDao.buscarClientes(textoNormalizado)));
			
		}catch(Exception e) {
			log.error("Error al consultar los clientes: ",e);
			return ResponseBuilder.buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "500", "Error en el Sistema");
		}
	}

	//===================================================================//
	////////////////////////GUARDAR CLIENTES///////////////////////////////
	//===================================================================//	
	
	@Override
	@Transactional
	public ResponseEntity<ResponseRestObject<ClienteDTO>> saveCliente(ClienteDTO clienteDTO) {
		try {
			//Validaciones Basicas
			String error = validarCliente(clienteDTO, null);
			
			if(error != null) {
				return ResponseBuilder.buildErrorResponseObject(HttpStatus.BAD_REQUEST, "400", error);
			}
			
			//Combierto cliente DTO a la entidad
			Cliente cliente = ClienteMapper.toEntity(clienteDTO);
			//Guardo la entidad
			Cliente clienteSave = clienteDao.save(cliente);
			
			return ResponseBuilder.buildSuccessResponseObject(ClienteMapper.toDTO(clienteSave));	
		}catch(Exception e) {
			log.error("Error al guardar los clientes: ",e);
			return ResponseBuilder.buildErrorResponseObject(HttpStatus.INTERNAL_SERVER_ERROR, "500", "Error en el Sistema");
		}
	}

	//===================================================================//
	////////////////////////ACTUALIZAR CLIENTES////////////////////////////
	//===================================================================//		
	
	@Override
	@Transactional
	public ResponseEntity<ResponseRestObject<ClienteDTO>> updateCliente(ClienteDTO clienteDTO, Long id) {
		try {		
			//Validaciones Basicas
			String error = validarCliente(clienteDTO,id);
			
			if(error != null) {
				return ResponseBuilder.buildErrorResponseObject(HttpStatus.BAD_REQUEST, "400", error);
			}
			
			return clienteDao.findById(id)
					.map(clienteSearch -> {
						ActualizarNewAOldCliente(clienteSearch, clienteDTO);
						Cliente clienteSave = clienteDao.save(clienteSearch);
						return ResponseBuilder.buildSuccessResponseObject(ClienteMapper.toDTO(clienteSave));
					})
					.orElseGet(() -> ResponseBuilder.buildErrorResponseObject(HttpStatus.NOT_FOUND, "404", "No se Encontro el cliente"));
			
		}catch(Exception e) {
			log.error("Error al actualizar los clientes: ",e);
			return ResponseBuilder.buildErrorResponseObject(HttpStatus.INTERNAL_SERVER_ERROR, "500", "Error en el Sistema");
		}
	}
	
	//===================================================================//
	////////////////////////ELIMINAR CLIENTES//////////////////////////////
	//===================================================================//	

	@Override
	@Transactional
	public ResponseEntity<ResponseRestObject<ClienteDTO>> deleteClienteById(Long id) {
		try {
			return clienteDao.findById(id)
					.map(clienteSearch -> {
						
						clienteDao.deleteById(id);
						return ResponseBuilder.buildSuccessResponseObject(ClienteMapper.toDTO(clienteSearch));
					})
					.orElseGet(() -> ResponseBuilder.buildErrorResponseObject(HttpStatus.NOT_FOUND, "404", "No se Encontro el cliente"));
			
		}catch(Exception e) {
			log.error("Error al actualizar los clientes: ",e);
			return ResponseBuilder.buildErrorResponseObject(HttpStatus.INTERNAL_SERVER_ERROR, "500", "Error en el Sistema");
		}
	}
	
	
	//===================================================================//
	/////////////////////////////HELPERS///////////////////////////////////
	//===================================================================//	
	
	public void ActualizarNewAOldCliente(Cliente clienteExistente,ClienteDTO clienteNuevo) {
		if (clienteNuevo.getNombre() != null) clienteExistente.setNombre(clienteNuevo.getNombre());
		
		clienteExistente.setDireccion(clienteNuevo.getDireccion());
		clienteExistente.setRfc(clienteNuevo.getRfc());
		
		if (clienteNuevo.getCorreo() != null) clienteExistente.setCorreo(clienteNuevo.getCorreo());
		
		if (clienteNuevo.getTelefono() != null)clienteExistente.setTelefono(clienteNuevo.getTelefono().trim());
	}
	
	
	//===================================================================//
	/////////////////////////////VALIDACIONES//////////////////////////////
	//===================================================================//	
	
	private String validarCliente(ClienteDTO clienteDto, Long clienteId) {
		
		if(clienteDto == null) {
			return "Los datos del cliente son obligatorios";
		}
		
		if(clienteDto.getNombre() == null || clienteDto.getNombre().trim().isEmpty()) {
			return "El nombre es obligatorio";
		}
		
		clienteDto.setNombre(clienteDto.getNombre().trim());
		
		if(clienteDto.getCorreo() != null && !clienteDto.getCorreo().trim().isEmpty()) {
			
			clienteDto.setCorreo(clienteDto.getCorreo().trim());
			
			if(!clienteDto.getCorreo().matches("^[\\w.-]+@[\\w.-]+\\.\\w+$")) {
				return "Correo electronico invalido";
			}
			
			boolean correoDuplicado = clienteId == null 
					? clienteDao.existsByCorreo(clienteDto.getCorreo()) 
					: clienteDao.existsByCorreoAndIdNot(clienteDto.getCorreo(), clienteId);
			
			if(correoDuplicado) {
				return "Ya existe un cliente con ese correo";
			}
		}
		
		if(clienteDto.getRfc() != null && !clienteDto.getRfc().trim().isEmpty()) {
			
			clienteDto.setRfc(clienteDto.getRfc().trim().toUpperCase());
			
			boolean rfcDuplicado = clienteId == null
					? clienteDao.existsByRfc(clienteDto.getRfc())
					: clienteDao.existsByRfcAndIdNot(clienteDto.getRfc(), clienteId);
			
			if(rfcDuplicado) {
				return "Ya existe un cliente con ese RFC";
			}
		}
		
		return null;
	}
	
}
