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

import com.bolsadeideas.backend.muebleria.dao.request.ActualizarDisenoMuebleRequest;
import com.bolsadeideas.backend.muebleria.dao.request.CrearDisenoMuebleRequest;
import com.bolsadeideas.backend.muebleria.dtos.DisenoMuebleDTO;
import com.bolsadeideas.backend.muebleria.model.CategoriaMueble;
import com.bolsadeideas.backend.muebleria.response.ResponseRest;
import com.bolsadeideas.backend.muebleria.response.ResponseRestObject;
import com.bolsadeideas.backend.muebleria.services.IDisenoMuebleServices;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/disenos-muebles")
@RequiredArgsConstructor
public class DisenoMueblesControlls {
	
	private final IDisenoMuebleServices disenoServices;

    @GetMapping
    public ResponseEntity<ResponseRest<DisenoMuebleDTO>>search() {
        return disenoServices.search();
    }

    @GetMapping("/activos")
    public ResponseEntity<ResponseRest<DisenoMuebleDTO>>searchActivos() {
        return disenoServices.searchActivos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseRestObject<DisenoMuebleDTO>>searchById(@PathVariable Long id) {
        return disenoServices.searchById(id);
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<ResponseRest<DisenoMuebleDTO>>searchByCategoria(@PathVariable CategoriaMueble categoria) {
        return disenoServices.searchByCategoria(categoria);
    }

    @GetMapping("/buscar")
    public ResponseEntity<ResponseRest<DisenoMuebleDTO>> buscar(@RequestParam String texto) {
        return disenoServices.buscar(texto);
    }

    @PostMapping
    public ResponseEntity<ResponseRestObject<DisenoMuebleDTO>> save(@Valid @RequestBody CrearDisenoMuebleRequest request) {
        return disenoServices.save(request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseRestObject<DisenoMuebleDTO>> update(@PathVariable Long id,
            @Valid
            @RequestBody ActualizarDisenoMuebleRequest request) {
    	
        return disenoServices.update(id, request);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<ResponseRestObject<DisenoMuebleDTO>> cambiarEstado(@PathVariable Long id, @RequestParam boolean activo) {
        return disenoServices.cambiarEstado(id, activo);
    }
}
