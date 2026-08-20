package com.bolsadeideas.backend.muebleria.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bolsadeideas.backend.muebleria.dtos.ClienteDTO;
import com.bolsadeideas.backend.muebleria.response.ResponseRest;
import com.bolsadeideas.backend.muebleria.response.ResponseRestObject;
import com.bolsadeideas.backend.muebleria.services.IClientesServices;


@RestController
@RequestMapping("/api/v1/clientes")
public class ClienteController {

	@Autowired
	private IClientesServices service;
	
	@GetMapping
	public ResponseEntity<ResponseRest<ClienteDTO>> searchCliente(){
		return service.searchClientes();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ResponseRestObject<ClienteDTO>> searchClienteById(@PathVariable Long id){
		return service.searchClienteById(id);
	}
	
	@GetMapping("/buscar")
	public ResponseEntity<ResponseRest<ClienteDTO>> buscarClientes(@RequestParam String texto){
		return service.buscarClientes(texto);
	}
	
	@PostMapping
	public ResponseEntity<ResponseRestObject<ClienteDTO>> saveCliente(@RequestBody ClienteDTO clienteDTO){
		return service.saveCliente(clienteDTO);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ResponseRestObject<ClienteDTO>> updateCliente(@PathVariable Long id, @RequestBody ClienteDTO clienteDTO){
		return service.updateCliente(clienteDTO, id);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseRestObject<ClienteDTO>> deleteCliente(@PathVariable Long id){
		return service.deleteClienteById(id);
	}
}
