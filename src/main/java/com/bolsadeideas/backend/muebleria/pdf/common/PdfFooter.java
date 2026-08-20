package com.bolsadeideas.backend.muebleria.pdf.common;

import com.lowagie.text.Element;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

public class PdfFooter {
	
	public static PdfPTable crear() {

        PdfPTable tabla = new PdfPTable(1);

        tabla.setWidthPercentage(100);


        /************************************************************/
        /********************** CONDICIONES *************************/
        /************************************************************/

        PdfPCell condiciones = new PdfPCell();

        condiciones.setBorder(Rectangle.NO_BORDER);
        condiciones.setPaddingTop(PdfStyles.SPACE_MEDIUM);
        condiciones.setPaddingBottom(PdfStyles.SPACE_MEDIUM);


        Paragraph vigencia = new Paragraph(
                "Este presupuesto tiene una vigencia de 15 días naturales.", PdfStyles.FONT_TEXT_MUTED);

        Paragraph iva = new Paragraph(
                "Los precios no incluyen IVA.", PdfStyles.FONT_TEXT_MUTED);

        Paragraph fabricacion = new Paragraph(
                "La fabricación inicia una vez confirmado el anticipo correspondiente.", PdfStyles.FONT_TEXT_MUTED);


        condiciones.addElement(vigencia);
        condiciones.addElement(iva);
        condiciones.addElement(fabricacion);

        tabla.addCell(condiciones);


        /************************************************************/
        /******************** AGRADECIMIENTO ************************/
        /************************************************************/

        PdfPCell agradecimiento = new PdfPCell();

        agradecimiento.setBorder(Rectangle.TOP );
        agradecimiento.setBorderColor(PdfStyles.COLOR_BORDER);
        agradecimiento.setBorderWidthTop(PdfStyles.BORDER_WIDTH);
        agradecimiento.setPaddingTop(PdfStyles.SPACE_MEDIUM);

        Paragraph texto = new Paragraph("Gracias por confiar en Mueblerías Kuko.", PdfStyles.FONT_LABEL);

        texto.setAlignment(Element.ALIGN_CENTER);

        agradecimiento.addElement(texto);

        tabla.addCell(agradecimiento);

        return tabla;
    }
    
	
}
