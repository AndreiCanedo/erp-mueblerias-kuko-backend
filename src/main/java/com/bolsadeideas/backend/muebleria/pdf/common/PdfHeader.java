package com.bolsadeideas.backend.muebleria.pdf.common;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PdfHeader {
	
	private final PdfEmpresaConfig empresaConfig;
	
	public PdfPTable crear(String tituloDocumento, String folio, LocalDateTime fecha, Image logo) {

        PdfPTable tabla = new PdfPTable(3);

        tabla.setWidthPercentage(100);

        try {
            tabla.setWidths(new float[] { 1.1f, 3.2f, 2.0f });
        } catch (Exception e) {
            throw new IllegalStateException("No fue posible configurar el encabezado del PDF", e);
        }


        /********************************************************/
        /*********************** LOGO ***************************/
        /********************************************************/

        PdfPCell celdaLogo = new PdfPCell();

        celdaLogo.setBorder(Rectangle.NO_BORDER);
        celdaLogo.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celdaLogo.setPadding(PdfStyles.CELL_PADDING_MEDIUM);
        celdaLogo.setPaddingTop(15f);


        if (logo != null) {

            logo.scaleToFit(PdfStyles.LOGO_MAX_WIDTH, PdfStyles.LOGO_MAX_HEIGHT);

            celdaLogo.addElement(logo);
        }
        
        /************************************************************/
        /******************* DATOS EMPRESA **************************/
        /************************************************************/

        PdfPCell celdaEmpresa = new PdfPCell();

        celdaEmpresa.setBorder(Rectangle.NO_BORDER);
        celdaEmpresa.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celdaEmpresa.setPadding(PdfStyles.CELL_PADDING_MEDIUM);

        Paragraph empresa = new Paragraph(empresaConfig.getNombre(), PdfStyles.FONT_EMPRESA);
        empresa.setSpacingAfter(PdfStyles.SPACE_SMALL);

        Paragraph rfc = new Paragraph("RFC: " + empresaConfig.getRfc(), PdfStyles.FONT_EMPRESA_INFO);

        Paragraph telefonos = new Paragraph(empresaConfig.obtenerTelefonosVentas(), PdfStyles.FONT_EMPRESA_INFO);

        Paragraph correo = new Paragraph(empresaConfig.getCorreo(), PdfStyles.FONT_EMPRESA_INFO);

        celdaEmpresa.addElement(empresa);
        celdaEmpresa.addElement(rfc);
        celdaEmpresa.addElement(telefonos);
        celdaEmpresa.addElement(correo);

        /********************************************************/
        /********************* DOCUMENTO  ***********************/
        /********************************************************/

        PdfPCell celdaDocumento = new PdfPCell();

        celdaDocumento.setBorder(Rectangle.NO_BORDER);
        celdaDocumento.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celdaDocumento.setHorizontalAlignment(Element.ALIGN_RIGHT);
        celdaDocumento.setPadding(PdfStyles.CELL_PADDING_MEDIUM);

        Paragraph documento = new Paragraph(tituloDocumento, PdfStyles.FONT_DOCUMENTO);

        documento.setAlignment(Element.ALIGN_RIGHT);
        documento.setSpacingAfter(PdfStyles.SPACE_SMALL);

        Paragraph folioParagraph = new Paragraph("Folio: " + folio, PdfStyles.FONT_FOLIO);

        folioParagraph.setAlignment(Element.ALIGN_RIGHT);

        Paragraph fechaParagraph = new Paragraph("Fecha: " + PdfUtils.formatearFecha(fecha), PdfStyles.FONT_TEXT_MUTED);

        fechaParagraph.setAlignment(Element.ALIGN_RIGHT);

        celdaDocumento.addElement(documento);
        celdaDocumento.addElement(folioParagraph);
        celdaDocumento.addElement(fechaParagraph);

        /********************************************************/
        /***************** AGREGAR CELDAS ************************/
        /********************************************************/

        tabla.addCell(celdaLogo);
        tabla.addCell(celdaEmpresa);
        tabla.addCell(celdaDocumento);
        tabla.setSpacingAfter(PdfStyles.SPACE_LARGE);

        return tabla;
    }
	
}
