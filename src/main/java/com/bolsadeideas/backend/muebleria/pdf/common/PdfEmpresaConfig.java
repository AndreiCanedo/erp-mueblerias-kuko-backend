package com.bolsadeideas.backend.muebleria.pdf.common;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "empresa")
@Getter
@Setter
public class PdfEmpresaConfig {

	/************************************************************/
    /************************ EMPRESA ***************************/
    /************************************************************/

    private String nombre;


    private String rfc;


    /************************************************************/
    /************************ CONTACTO **************************/
    /************************************************************/

    private String correo;

    private String telefono1;

    private String telefono2;


    /************************************************************/
    /********************** CONDICIONES *************************/
    /************************************************************/

    public static final String VIGENCIA_PRESUPUESTO =
        "Este presupuesto tiene una vigencia de 15 días naturales.";


    public static final String MENSAJE_IVA =
        "Los precios no incluyen IVA.";


    public static final String MENSAJE_FABRICACION =
        "La fabricacion inicia una vez confirmado el anticipo correspondiente.";


    /************************************************************/
    /********************** MENSAJES ****************************/
    /************************************************************/

    public static final String AGRADECIMIENTO =
        "Gracias por confiar en Mueblerías Kuko.";
    
    /************************************************************/
    /*********************** HELPERS ****************************/
    /************************************************************/
    
    public String obtenerTelefonosVentas() {

    	return "Ventas: " + telefono1 + " / " + telefono2;
    }
    
	
}
