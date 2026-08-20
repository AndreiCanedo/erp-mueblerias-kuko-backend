package com.bolsadeideas.backend.muebleria.control;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bolsadeideas.backend.muebleria.dao.request.ActualizarUsuarioRequest;
import com.bolsadeideas.backend.muebleria.dao.request.CrearUsuarioRequest;
import com.bolsadeideas.backend.muebleria.dtos.UsuarioDTO;
import com.bolsadeideas.backend.muebleria.response.ResponseRest;
import com.bolsadeideas.backend.muebleria.response.ResponseRestObject;
import com.bolsadeideas.backend.muebleria.services.IUsuarioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
	
	private final IUsuarioService usuarioService;
	
	@GetMapping
    public ResponseEntity<ResponseRest<UsuarioDTO>> listar() {

        return usuarioService.search();
    }
	
	@GetMapping("/{id}")
    public ResponseEntity<ResponseRestObject<UsuarioDTO>> buscarPorId(@PathVariable Long id) {

        return usuarioService.searchById(id);
    }
	
	@PostMapping
    public ResponseEntity<ResponseRestObject<UsuarioDTO>> crear(@Valid @RequestBody CrearUsuarioRequest request) {

        return usuarioService.save(request);
    }
	
	@PutMapping("/{id}")
    public ResponseEntity<ResponseRestObject<UsuarioDTO>> actualizar(@PathVariable Long id, @Valid @RequestBody ActualizarUsuarioRequest request) {

        return usuarioService.update(id, request);
    }
	
	@PatchMapping("/{id}/estado")
    public ResponseEntity<ResponseRestObject<UsuarioDTO>> cambiarEstado(@PathVariable Long id, @RequestParam boolean activo) {

        return usuarioService.cambiarEstado(id, activo);
    }

}
