package com.bolsadeideas.backend.muebleria.pdf.common;

import java.util.List;

import com.bolsadeideas.backend.muebleria.dtos.DocumentoComercialPdfDetalleDTO;
import com.lowagie.text.Element;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

public class PdfDetalle {
	
	public static PdfPTable crear(List<DocumentoComercialPdfDetalleDTO> detalles) {

    	PdfPTable tabla = new PdfPTable(4);

    	tabla.setWidthPercentage(100);
    	tabla.setHeaderRows(2);
    	tabla.setSplitRows(true);
    	tabla.setSplitLate(false);

    	try {

    		tabla.setWidths(new float[] { 0.8f, 4.2f, 1.6f, 1.6f});

    	} catch (Exception e) {

    	    throw new IllegalStateException("No fue posible configurar la tabla de detalles", e);
    	}


    	/************************************************************/
   	    /********************* TITULO SECCION ***********************/
   	    /************************************************************/

   	    PdfPCell titulo = new PdfPCell(new Paragraph("DETALLE DEL PRESUPUESTO", PdfStyles.FONT_SECTION_TITLE));

   	    titulo.setColspan(4);
   	    titulo.setPadding(PdfStyles.CELL_PADDING_MEDIUM);
   	    titulo.setBackgroundColor(PdfStyles.COLOR_PRIMARY_SOFT);
   	    titulo.setBorder(Rectangle.NO_BORDER);
   	    tabla.addCell(titulo);


   	    /************************************************************/
   	    /********************* ENCABEZADOS **************************/
   	    /************************************************************/

   	    agregarEncabezadoTabla(tabla, "Cant.");
   	    agregarEncabezadoTabla(tabla, "Descripción");
    	agregarEncabezadoTabla(tabla, "P. Unit.");
    	agregarEncabezadoTabla(tabla, "Total");


    	/************************************************************/
    	/************************ DETALLES **************************/
    	/************************************************************/

    	if (detalles == null || detalles.isEmpty()) {

    		PdfPCell sinDetalles = new PdfPCell(new Paragraph("Sin muebles registrados", PdfStyles.FONT_TEXT_MUTED));

    		sinDetalles.setColspan(4);
    		sinDetalles.setHorizontalAlignment(Element.ALIGN_CENTER);
    		sinDetalles.setPadding(PdfStyles.CELL_PADDING_MEDIUM);
    	    sinDetalles.setBorderColor(PdfStyles.COLOR_BORDER);

    	    tabla.addCell(sinDetalles);

    	} else {

    		for (int i = 0; i < detalles.size(); i++) {
    			
    			DocumentoComercialPdfDetalleDTO detalle = detalles.get(i);
    			
    			boolean alterna = i % 2 != 0;
    			
    			agregarCeldaDetalle(tabla, PdfUtils.formatearCantidad(detalle.getCantidad()), Element.ALIGN_CENTER, alterna);
    			agregarCeldaDetalle(tabla, detalle.getDescripcion(), Element.ALIGN_LEFT, alterna);
    			agregarCeldaDetalle(tabla, PdfUtils.formatearMoneda(detalle.getPrecioUnitario()), Element.ALIGN_RIGHT, alterna);
    			agregarCeldaDetalle(tabla, PdfUtils.formatearMoneda(detalle.getPrecioTotal()), Element.ALIGN_RIGHT, alterna);
    			
    		}
    	}


    	tabla.setSpacingAfter(PdfStyles.SPACE_LARGE);
    	
    	return tabla;
    }   
	
	 private static void agregarEncabezadoTabla( PdfPTable tabla, String texto) {

		 PdfPCell celda = new PdfPCell( new Paragraph(texto, PdfStyles.FONT_TABLE_HEADER));

		 celda.setHorizontalAlignment(Element.ALIGN_CENTER);
		 celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
		 celda.setPadding(PdfStyles.CELL_PADDING_MEDIUM );
		 celda.setBackgroundColor(PdfStyles.COLOR_PRIMARY);
		 celda.setBorderColor(PdfStyles.COLOR_PRIMARY);

		 tabla.addCell(celda);
	 }
	 
	 private static void agregarCeldaDetalle(PdfPTable tabla, String texto, int alineacion, boolean alterna) {

		 PdfPCell celda = new PdfPCell(new Paragraph(
				 texto == null || texto.isBlank()? "-" : texto, PdfStyles.FONT_TABLE_TEXT));


		 /************************************************************/
		 /*********************** ALINEACION *************************/
		 /************************************************************/

		 celda.setHorizontalAlignment(alineacion);
		 celda.setVerticalAlignment(Element.ALIGN_MIDDLE);

		 /************************************************************/
		 /************************ ESPACIO ***************************/
		 /************************************************************/

		 celda.setPaddingTop(8f);
		 celda.setPaddingBottom(8f);
		 celda.setPaddingLeft(6f);
		 celda.setPaddingRight(6f);

		 /************************************************************/
		 /************************* BORDE ****************************/
		 /************************************************************/

		 celda.setBorder(Rectangle.BOTTOM);
		 celda.setBorderColor(PdfStyles.COLOR_BORDER);
		 celda.setBorderWidthBottom(PdfStyles.BORDER_WIDTH);

		 /************************************************************/
		 /********************** FILA ALTERNA ************************/
		 /************************************************************/

		 if (alterna) {
			 
			 celda.setBackgroundColor(PdfStyles.COLOR_ROW_ALTERNATE);
		 }


		 tabla.addCell(celda);
	 }
	
}
