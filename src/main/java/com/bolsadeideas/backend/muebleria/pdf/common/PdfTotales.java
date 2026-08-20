package com.bolsadeideas.backend.muebleria.pdf.common;

import java.math.BigDecimal;

import com.bolsadeideas.backend.muebleria.dtos.DocumentoComercialPdfDTO;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

public class PdfTotales {
	
	public static PdfPTable crear(DocumentoComercialPdfDTO presupuesto) {

    	PdfPTable contenedor =new PdfPTable(2);
    	
    	contenedor.setWidthPercentage(100);

    	try {
    		contenedor.setWidths(new float[] { 1.2f, 1f });

    	} catch (Exception e) {

    		throw new IllegalStateException("No fue posible configurar el resumen financiero", e);
    	}


    	/************************************************************/
    	/******************* ESPACIO IZQUIERDO **********************/
    	/************************************************************/

    	PdfPCell espacio = new PdfPCell();

    	espacio.setBorder(Rectangle.NO_BORDER);


    	/************************************************************/
    	/******************* TABLA DE TOTALES ***********************/
    	/************************************************************/

    	PdfPTable tablaTotales =new PdfPTable(2);

    	tablaTotales.setWidthPercentage(100);

    	try {
    		tablaTotales.setWidths(new float[] { 1.4f, 1f });

    	} catch (Exception e) {

    		throw new IllegalStateException("No fue posible configurar los totales", e);
    	}


    	/************************************************************/
    	/********************* TITULO *******************************/
    	/************************************************************/

    	PdfPCell titulo = new PdfPCell(new Paragraph("RESUMEN FINANCIERO", PdfStyles.FONT_SECTION_TITLE));

    	titulo.setColspan(2);
    	titulo.setPaddingBottom(PdfStyles.SPACE_SMALL);
    	titulo.setBorder(Rectangle.NO_BORDER);
    	tablaTotales.addCell(titulo);
    	
    	/************************************************************/
    	/************************ TOTAL *****************************/
    	/************************************************************/
    	
    	agregarFilaTotal(tablaTotales, "Total", presupuesto.getTotal(), false);
    	
    	/************************************************************/
    	/******************** TOTAL PAGADO *************************/
    	/************************************************************/

    	agregarFilaTotal(tablaTotales, "Total pagado", presupuesto.getTotalPagado(), false);

    	/************************************************************/
    	/******************** SALDO PENDIENTE ***********************/
    	/************************************************************/

    	agregarFilaTotal(tablaTotales, "Saldo pendiente", presupuesto.getSaldoPendiente(), true);

    	/************************************************************/
    	/********************** CONTENEDOR **************************/
    	/************************************************************/

    	PdfPCell celdaTotales = new PdfPCell(tablaTotales);

    	celdaTotales.setBorder(Rectangle.NO_BORDER);
    	
    	contenedor.addCell(espacio);
    	contenedor.addCell(celdaTotales);
    	contenedor.setSpacingAfter(PdfStyles.SPACE_LARGE);
    	
    	return contenedor;
    }
	
	private static void agregarFilaTotal(PdfPTable tabla, String etiqueta, BigDecimal valor, boolean destacado) {

    	Font fuenteEtiqueta = destacado ? PdfStyles.FONT_TOTAL_LABEL : PdfStyles.FONT_TEXT;


    	Font fuenteValor = destacado ? PdfStyles.FONT_TOTAL : PdfStyles.FONT_LABEL;


    	PdfPCell celdaEtiqueta = new PdfPCell(new Paragraph(etiqueta, fuenteEtiqueta));

    	celdaEtiqueta.setPadding(PdfStyles.CELL_PADDING_MEDIUM);
    	celdaEtiqueta.setBorderColor(PdfStyles.COLOR_BORDER);
    	celdaEtiqueta.setBorderWidth(PdfStyles.BORDER_WIDTH);

    	PdfPCell celdaValor = new PdfPCell(new Paragraph(PdfUtils.formatearMoneda(valor), fuenteValor));

    	celdaValor.setHorizontalAlignment( Element.ALIGN_RIGHT);
    	celdaValor.setPadding(PdfStyles.CELL_PADDING_MEDIUM);
    	celdaValor.setBorderColor(PdfStyles.COLOR_BORDER);
    	celdaValor.setBorderWidth(PdfStyles.BORDER_WIDTH);

    	if (destacado) {

    		celdaEtiqueta.setBackgroundColor(PdfStyles.COLOR_PRIMARY_SOFT);
    		celdaValor.setBackgroundColor(PdfStyles.COLOR_PRIMARY_SOFT);
    	}

    	tabla.addCell(celdaEtiqueta);
    	tabla.addCell(celdaValor);
    }
    
	
}
