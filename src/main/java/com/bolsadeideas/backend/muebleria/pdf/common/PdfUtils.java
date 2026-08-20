package com.bolsadeideas.backend.muebleria.pdf.common;

import java.math.BigDecimal;
import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import com.lowagie.text.Image;

public class PdfUtils {
	
	private PdfUtils() {
		
	}
	
	/************************************************************/
    /************************ FECHAS ****************************/
    /************************************************************/

    public static String formatearFecha(LocalDateTime fecha) {

        if (fecha == null) {
            return "";
        }

        return fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }


    public static String formatearFecha(LocalDate fecha) {

        if (fecha == null) {
            return "";
        }

        return fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }


    /************************************************************/
    /************************ MONEDA ****************************/
    /************************************************************/

    public static String formatearMoneda(BigDecimal valor) {

        if (valor == null) {
            valor = BigDecimal.ZERO;
        }

        NumberFormat formato = NumberFormat.getCurrencyInstance(Locale.of("es", "MX"));

        return formato.format(valor);
    }


    /************************************************************/
    /************************ TEXTO *****************************/
    /************************************************************/

    public static String valorSeguro(String valor) {

        if (valor == null || valor.isBlank()) {
            return "-";
        }

        return valor.trim();
    }


    /************************************************************/
    /************************ CANTIDAD **************************/
    /************************************************************/

    public static String formatearCantidad(Double cantidad) {

        if (cantidad == null) {
            return "0";
        }

        if (cantidad.doubleValue() == cantidad.longValue()) {

            return String.valueOf(cantidad.longValue());
        }

        return String.valueOf(cantidad);
    }
    
    public static Image cargarLogo() {

        try {

            URL recurso =
                PdfUtils.class.getResource("/static/img/logo.png");

            if (recurso == null) {
                return null;
            }

            return Image.getInstance(
                recurso
            );

        } catch (Exception e) {

            return null;
        }
    }
	
}
