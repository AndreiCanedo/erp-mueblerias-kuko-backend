package com.bolsadeideas.backend.muebleria.pdf.OrdenCompraPdf;

import java.io.ByteArrayOutputStream;

import org.springframework.stereotype.Service;

import com.bolsadeideas.backend.muebleria.dtos.OrdenCompraPdfDTO;
import com.bolsadeideas.backend.muebleria.dtos.DocumentoComercialPdfDTO;
import com.bolsadeideas.backend.muebleria.pdf.common.PdfCliente;
import com.bolsadeideas.backend.muebleria.pdf.common.PdfDetalle;
import com.bolsadeideas.backend.muebleria.pdf.common.PdfFooter;
import com.bolsadeideas.backend.muebleria.pdf.common.PdfHeader;
import com.bolsadeideas.backend.muebleria.pdf.common.PdfTotales;
import com.bolsadeideas.backend.muebleria.pdf.common.PdfUtils;
import com.lowagie.text.Image;
import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfWriter;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrdenCompraPdfService {
	
	private final OrdenCompraPdfDataService ordenCompraPdfDataService;
	
	private final PdfHeader pdfHeader;

    public byte[] generarPdf(Long ordenId) {

        OrdenCompraPdfDTO orden = ordenCompraPdfDataService.obtenerDatosOrden(ordenId);

        DocumentoComercialPdfDTO documento = orden.getDocumento();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Document pdf = new Document(PageSize.A4, 36, 36, 36, 36);

        try {

            PdfWriter.getInstance(pdf, outputStream);

            pdf.open();

            Image logo = PdfUtils.cargarLogo();

            pdf.add(pdfHeader.crear(
            		"ORDEN DE COMPRA", String.valueOf(documento.getFolio()), documento.getFecha(), logo));

            pdf.add(PdfCliente.crear(documento));
            pdf.add(PdfEstadoOrden.crear(orden));
            pdf.add(PdfDetalle.crear(documento.getDetalles()));
            pdf.add(PdfTotales.crear(documento));
            pdf.add(PdfFooter.crear());

        } catch (Exception e) {

            throw new IllegalStateException( "No fue posible generar el PDF de la orden", e);

        } finally {

            if (pdf.isOpen()) {
                pdf.close();
            }
        }

        return outputStream.toByteArray();
    }
    

	
}
