package com.bolsadeideas.backend.muebleria.pdf.common;

import com.bolsadeideas.backend.muebleria.dtos.DocumentoComercialPdfDTO;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

public class PdfCliente {
	
	 public static PdfPTable crear(DocumentoComercialPdfDTO presupuesto) {

	    	PdfPTable tabla = new PdfPTable(2);

	    	tabla.setWidthPercentage(100);

	    	try {

	    		tabla.setWidths(new float[] { 1.7f, 1f });

	    	} catch (Exception e) {

	    		throw new IllegalStateException("No fue posible configurar los datos del cliente", e);
	    	}

	    	/************************************************************/
	    	/******************** TITULO SECCION ************************/
	    	/************************************************************/

	    	PdfPCell titulo = new PdfPCell(new Paragraph("DATOS DEL CLIENTE",PdfStyles.FONT_SECTION_TITLE));

	    	titulo.setColspan(2);
	    	titulo.setBorder(Rectangle.NO_BORDER);
	   	    titulo.setPaddingBottom(PdfStyles.SPACE_SMALL);

	   	    tabla.addCell(titulo);
	   	    
	   	    /************************************************************/
	   	    /******************** BLOQUE PRINCIPAL **********************/
	   	    /************************************************************/
	   	    
	   	    PdfPCell principal = new PdfPCell();

	   	    principal.setBorder(Rectangle.NO_BORDER);
	   	    principal.setBackgroundColor(PdfStyles.COLOR_BACKGROUND);
	   	    principal.setPadding(PdfStyles.CELL_PADDING_MEDIUM);

	   	    Paragraph nombre = new Paragraph(PdfUtils.valorSeguro(presupuesto.getClienteNombre()), PdfStyles.FONT_LABEL);

	   	    nombre.setSpacingAfter(4f);

	   	    principal.addElement(nombre);
	   	    principal.addElement(new Paragraph("Tel. " + PdfUtils.valorSeguro(presupuesto.getClienteTelefono()), PdfStyles.FONT_TEXT));
	   	    principal.addElement(new Paragraph(PdfUtils.valorSeguro(presupuesto.getClienteCorreo()), PdfStyles.FONT_TEXT));
	   	    principal.addElement(new Paragraph(PdfUtils.valorSeguro(presupuesto.getClienteDireccion()), PdfStyles.FONT_TEXT));
	   	    
	   	    /************************************************************/
	   	    /******************** DATOS SECUNDARIOS *********************/
	   	    /************************************************************/

	   	    PdfPCell secundarios = new PdfPCell();

	   	    secundarios.setBorder(Rectangle.NO_BORDER);
	   	    secundarios.setBackgroundColor(PdfStyles.COLOR_PRIMARY_SOFT);
	   	    secundarios.setPadding(PdfStyles.CELL_PADDING_MEDIUM);
	   	    
	   	    if (presupuesto.getClienteRfc() != null && !presupuesto.getClienteRfc().isBlank()) {

	             secundarios.addElement( new Paragraph("RFC", PdfStyles.FONT_TEXT_MUTED));

	             Paragraph rfc = new Paragraph(presupuesto.getClienteRfc().trim(), PdfStyles.FONT_LABEL);

	             rfc.setSpacingAfter(PdfStyles.SPACE_SMALL);

	             secundarios.addElement(rfc);
	        }
	   	    
	   	    secundarios.addElement( new Paragraph("Fecha de entrega", PdfStyles.FONT_TEXT_MUTED));
	   	    secundarios.addElement( new Paragraph(PdfUtils.formatearFecha(presupuesto.getFechaEntrega()), PdfStyles.FONT_LABEL));


	   	    tabla.addCell(principal);
	   	    tabla.addCell(secundarios);

	   	    tabla.setSpacingAfter(PdfStyles.SPACE_LARGE);

	   	    return tabla;

	   	}
	    
	
}
