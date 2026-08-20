package com.bolsadeideas.backend.muebleria.dtos;


import java.time.LocalDateTime;

import com.bolsadeideas.backend.muebleria.model.EstadoOrdenCompra;
import com.bolsadeideas.backend.muebleria.model.EstadoPago;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrdenCompraPdfDTO {
	
	private DocumentoComercialPdfDTO documento;

    private LocalDateTime fechaConfirmacion;
    private EstadoOrdenCompra estadoOrden;
    private EstadoPago estadoPago;
	
}
