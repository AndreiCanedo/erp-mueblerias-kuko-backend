package com.bolsadeideas.backend.muebleria.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentoComercialPdfDTO {
	
	private Long folio;
    private LocalDateTime fecha;
    private LocalDate fechaEntrega;

    private Long clienteId;
    private String clienteNombre;
    private String clienteDireccion;
    private String clienteTelefono;
    private String clienteCorreo;
    private String clienteRfc;

    private List<DocumentoComercialPdfDetalleDTO> detalles;

    private BigDecimal total;
    private BigDecimal totalPagado;
    private BigDecimal saldoPendiente;

}
