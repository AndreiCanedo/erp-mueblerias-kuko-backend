package com.bolsadeideas.backend.muebleria.pdf.presupuestoPdf;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.backend.muebleria.dao.IOrdenCompraDao;
import com.bolsadeideas.backend.muebleria.dao.IPagoOrdenDao;
import com.bolsadeideas.backend.muebleria.dtos.DocumentoComercialPdfDTO;
import com.bolsadeideas.backend.muebleria.dtos.DocumentoComercialPdfDetalleDTO;
import com.bolsadeideas.backend.muebleria.model.Cliente;
import com.bolsadeideas.backend.muebleria.model.EstadoPagoOrden;
import com.bolsadeideas.backend.muebleria.model.OrdenCompra;
import com.bolsadeideas.backend.muebleria.model.OrdenDetalle;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PresupuestoPdfDataService {
	
	private final IOrdenCompraDao ordenDao;
    private final IPagoOrdenDao pagoOrdenDao;


    /************************************************************/
    /***************** CONSTRUIR PRESUPUESTO ********************/
    /************************************************************/

    @Transactional(readOnly = true)
    public DocumentoComercialPdfDTO obtenerDatosPresupuesto(Long ordenId) {

        OrdenCompra orden = ordenDao.findById(ordenId)
            .orElseThrow(() -> new IllegalArgumentException("No se encontró la orden de compra"));

        Cliente cliente = orden.getCliente();

        if (cliente == null) {
            throw new IllegalArgumentException("La orden no tiene un cliente asignado");
        }

        List<DocumentoComercialPdfDetalleDTO> detalles = construirDetalles(orden);

        BigDecimal total = orden.getTotal() != null ? orden.getTotal() : BigDecimal.ZERO;
        BigDecimal totalPagado = obtenerTotalPagado(orden.getId());
        BigDecimal saldoPendiente = calcularSaldoPendiente(total, totalPagado);

        return DocumentoComercialPdfDTO.builder()

            .folio(orden.getId())
            .fecha(orden.getFecha())
            .fechaEntrega(orden.getFechaEntrega())
            .clienteId(cliente.getId())
            .clienteNombre(normalizarTexto(cliente.getNombre()))
            .clienteDireccion(normalizarTexto(cliente.getDireccion()))
            .clienteTelefono(normalizarTexto(cliente.getTelefono()))
            .clienteCorreo(normalizarTexto(cliente.getCorreo()))
            .clienteRfc(normalizarTexto(cliente.getRfc()))
            .detalles(detalles)
            .total(total)
            .totalPagado(totalPagado)
            .saldoPendiente(saldoPendiente)
            .build();
    }


    /************************************************************/
    /********************** DETALLES ****************************/
    /************************************************************/

    private List<DocumentoComercialPdfDetalleDTO> construirDetalles(OrdenCompra orden) {

        if (orden.getDetalles() == null || orden.getDetalles().isEmpty()) {

            return List.of();
        }

        return orden.getDetalles()
            .stream()
            .map(this::construirDetalle)
            .toList();
    }


    private DocumentoComercialPdfDetalleDTO construirDetalle(OrdenDetalle detalle) {

        String descripcion = "";

        Long muebleId = null;

        if (detalle.getMueble() != null) {

            muebleId = detalle.getMueble().getId();
            descripcion = normalizarTexto(detalle.getMueble().getDescripcion());
        }

        BigDecimal precioUnitario = detalle.getPrecioUnitario() != null
                ? detalle.getPrecioUnitario()
                : BigDecimal.ZERO;


        BigDecimal precioTotal = detalle.getPrecioTotal();

        //Prevencion por si no hay registro de precio Total lo reconstruimos
        if (precioTotal == null) {

            double cantidad = detalle.getCantidad() != null ? detalle.getCantidad() : 0D;

            precioTotal = precioUnitario.multiply(BigDecimal.valueOf(cantidad));
        }


        return DocumentoComercialPdfDetalleDTO.builder()
            .cantidad(detalle.getCantidad())
            .muebleId(muebleId)
            .descripcion(descripcion)
            .precioUnitario(precioUnitario)
            .precioTotal(precioTotal)
            .build();
    }


    /************************************************************/
    /************************ PAGOS *****************************/
    /************************************************************/

    private BigDecimal obtenerTotalPagado(Long ordenId) {

        BigDecimal totalPagado = pagoOrdenDao
                .sumMontoByOrdenIdAndEstado(ordenId, EstadoPagoOrden.APLICADO);

        return totalPagado != null ? totalPagado : BigDecimal.ZERO;
    }


    /************************************************************/
    /*********************** CALCULOS ***************************/
    /************************************************************/

    private BigDecimal calcularSaldoPendiente(BigDecimal total, BigDecimal totalPagado) {

        BigDecimal saldo = total.subtract(totalPagado);

        //No mostraremos saldo negativo 
        if (saldo.compareTo(BigDecimal.ZERO) < 0) {

            return BigDecimal.ZERO;
        }

        return saldo;
    }


    /************************************************************/
    /************************ HELPERS ***************************/
    /************************************************************/

    private String normalizarTexto(String valor) {

        if (valor == null || valor.isBlank()) {

            return "";
        }

        return valor.trim();
    }
	
}
