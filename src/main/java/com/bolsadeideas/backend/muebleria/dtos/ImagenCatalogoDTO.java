package com.bolsadeideas.backend.muebleria.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImagenCatalogoDTO {

	private String imagenUrl;
	private String miniaturaUrl;
	
}
