package com.bolsadeideas.backend.muebleria.pdf.common;

import com.lowagie.text.Element;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

public class PdfFirmas {
	
	public static PdfPTable crear() {

        PdfPTable tabla = new PdfPTable(2);

        tabla.setWidthPercentage(100);

        try {

            tabla.setWidths(new float[] {1f, 1f});

        } catch (Exception e) {

            throw new IllegalStateException("No fue posible crear la sección de firmas", e);
        }


        /************************************************************/
        /********************* TITULO *******************************/
        /************************************************************/

        PdfPCell titulo = new PdfPCell(new Paragraph("AUTORIZACION", PdfStyles.FONT_SECTION_TITLE));

        titulo.setColspan(2);
        titulo.setBorder(Rectangle.NO_BORDER);
        titulo.setPaddingBottom(PdfStyles.SPACE_MEDIUM);
        tabla.addCell(titulo);


        /************************************************************/
        /******************* ESPACIO PARA FIRMAR ********************/
        /************************************************************/

        PdfPCell izquierda = crearCeldaFirma("Cliente");
        PdfPCell derecha = crearCeldaFirma("Mueblerías Kuko");

        tabla.addCell(izquierda);
        tabla.addCell(derecha);
        tabla.setSpacingBefore(PdfStyles.SPACE_LARGE);
        tabla.setSpacingAfter(PdfStyles.SPACE_LARGE);

        return tabla;
    }
    
	private static PdfPCell crearCeldaFirma(String titulo) {

    	PdfPCell celda = new PdfPCell();

    	celda.setBorder(Rectangle.NO_BORDER);
    	celda.setPaddingLeft(25f);
    	celda.setPaddingRight(25f);
    	celda.setPaddingTop(18f);
    	
    	/****************************************************/
    	/**************** ESPACIO FIRMA *********************/
    	/****************************************************/

    	Paragraph espacio = new Paragraph("\n\n");
    	
    	celda.addElement(espacio);

    	/****************************************************/
    	/******************* LINEA **************************/
    	/****************************************************/

    	Paragraph linea = new Paragraph("____________________________");

    	linea.setAlignment(Element.ALIGN_CENTER);

    	celda.addElement(linea);

    	/****************************************************/
    	/******************* TITULO *************************/
    	/****************************************************/

    	Paragraph nombre = new Paragraph(titulo, PdfStyles.FONT_LABEL);

    	nombre.setAlignment(Element.ALIGN_CENTER);
    	nombre.setSpacingBefore(5f);
    	    
    	celda.addElement(nombre);

    	return celda;
    }
	
}
