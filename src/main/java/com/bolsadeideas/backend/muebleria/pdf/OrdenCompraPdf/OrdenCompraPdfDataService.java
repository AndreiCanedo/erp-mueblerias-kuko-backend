package com.bolsadeideas.backend.muebleria.pdf.OrdenCompraPdf;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.backend.muebleria.dao.IOrdenCompraDao;
import com.bolsadeideas.backend.muebleria.dtos.OrdenCompraPdfDTO;
import com.bolsadeideas.backend.muebleria.dtos.DocumentoComercialPdfDTO;
import com.bolsadeideas.backend.muebleria.model.OrdenCompra;
import com.bolsadeideas.backend.muebleria.pdf.presupuestoPdf.PresupuestoPdfDataService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrdenCompraPdfDataService {
	
	private final IOrdenCompraDao ordenDao;
    private final PresupuestoPdfDataService presupuestoPdfDataService;

    @Transactional(readOnly = true)
    public OrdenCompraPdfDTO obtenerDatosOrden(Long ordenId) {

        OrdenCompra orden = ordenDao.findById(ordenId)
            .orElseThrow(() -> new IllegalArgumentException("No se encontró la orden de compra"));
        
        if (orden.getFechaConfirmacion() == null) {
            throw new IllegalStateException("La orden todavía no ha sido confirmada");
        }
        
        DocumentoComercialPdfDTO documento = presupuestoPdfDataService.obtenerDatosPresupuesto(ordenId);

        return OrdenCompraPdfDTO.builder()

            .documento(documento)
            .fechaConfirmacion(orden.getFechaConfirmacion())
            .estadoOrden(orden.getEstadoOrden())
            .estadoPago(orden.getEstadoPago())
            .build();
    }
	
}
