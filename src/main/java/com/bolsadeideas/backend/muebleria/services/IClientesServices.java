package com.bolsadeideas.backend.muebleria.services;

import org.springframework.http.ResponseEntity;

import com.bolsadeideas.backend.muebleria.dtos.ClienteDTO;
import com.bolsadeideas.backend.muebleria.response.ResponseRest;
import com.bolsadeideas.backend.muebleria.response.ResponseRestObject;


public interface IClientesServices {
	
	public ResponseEntity<ResponseRest<ClienteDTO>> searchClientes();
	public ResponseEntity<ResponseRestObject<ClienteDTO>> searchClienteById(Long id);
	public ResponseEntity<ResponseRest<ClienteDTO>> buscarClientes(String texto);
	public ResponseEntity<ResponseRestObject<ClienteDTO>> saveCliente(ClienteDTO cliente);
	public ResponseEntity<ResponseRestObject<ClienteDTO>> updateCliente(ClienteDTO cliente, Long id);
	public ResponseEntity<ResponseRestObject<ClienteDTO>> deleteClienteById(Long id);
}
