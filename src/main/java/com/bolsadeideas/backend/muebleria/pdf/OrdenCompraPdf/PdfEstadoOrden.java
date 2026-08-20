package com.bolsadeideas.backend.muebleria.pdf.OrdenCompraPdf;

import com.bolsadeideas.backend.muebleria.dtos.OrdenCompraPdfDTO;
import com.bolsadeideas.backend.muebleria.pdf.common.PdfStyles;
import com.bolsadeideas.backend.muebleria.pdf.common.PdfUtils;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

public class PdfEstadoOrden {
	
	private PdfEstadoOrden() {
    }
	
	public static PdfPTable crear(OrdenCompraPdfDTO orden) {

		PdfPTable tabla = new PdfPTable(2);

		tabla.setWidthPercentage(100);

		PdfPCell titulo = new PdfPCell(new Paragraph("DATOS DE LA ORDEN", PdfStyles.FONT_SECTION_TITLE));

		titulo.setColspan(2);
		titulo.setBorder(Rectangle.NO_BORDER);
		titulo.setPaddingBottom(PdfStyles.SPACE_SMALL);

		tabla.addCell(titulo);

		agregarCampo(tabla, "Fecha confirmación", PdfUtils.formatearFecha(orden.getFechaConfirmacion()));
		agregarCampo(tabla, "Estado", orden.getEstadoOrden() != null ? orden.getEstadoOrden().name() : "-");
		agregarCampo(tabla, "Estado de pago", orden.getEstadoPago() != null ? orden.getEstadoPago().name() : "-");

		tabla.setSpacingAfter(PdfStyles.SPACE_LARGE);

		return tabla;
	}

	private static void agregarCampo(PdfPTable tabla, String etiqueta, String valor) {
		
		PdfPCell label = new PdfPCell(new Paragraph(etiqueta, PdfStyles.FONT_LABEL));

		label.setBorder(Rectangle.NO_BORDER);

		PdfPCell data = new PdfPCell(new Paragraph(valor, PdfStyles.FONT_TEXT));

		data.setBorder(Rectangle.NO_BORDER);

		tabla.addCell(label);
		tabla.addCell(data);
	}
	
}
