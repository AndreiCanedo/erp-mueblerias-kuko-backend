package com.bolsadeideas.backend.muebleria.services;

import org.springframework.http.ResponseEntity;

import com.bolsadeideas.backend.muebleria.dao.request.ActualizarUsuarioRequest;
import com.bolsadeideas.backend.muebleria.dao.request.CrearUsuarioRequest;
import com.bolsadeideas.backend.muebleria.dtos.UsuarioDTO;
import com.bolsadeideas.backend.muebleria.response.ResponseRest;
import com.bolsadeideas.backend.muebleria.response.ResponseRestObject;

public interface IUsuarioService {
	
	ResponseEntity<ResponseRest<UsuarioDTO>> search();
    ResponseEntity<ResponseRestObject<UsuarioDTO>> searchById(Long id);
    ResponseEntity<ResponseRestObject<UsuarioDTO>> save(CrearUsuarioRequest request);
    ResponseEntity<ResponseRestObject<UsuarioDTO>> update(Long id, ActualizarUsuarioRequest request);
    ResponseEntity<ResponseRestObject<UsuarioDTO>> cambiarEstado(Long id, boolean activo);
	
}
