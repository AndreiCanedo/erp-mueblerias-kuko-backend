package com.bolsadeideas.backend.muebleria.pdf.presupuestoPdf;

import java.io.ByteArrayOutputStream;

import org.springframework.stereotype.Service;

import com.bolsadeideas.backend.muebleria.pdf.common.PdfCliente;
import com.bolsadeideas.backend.muebleria.pdf.common.PdfDetalle;
import com.bolsadeideas.backend.muebleria.pdf.common.PdfFirmas;
import com.bolsadeideas.backend.muebleria.pdf.common.PdfFooter;
import com.bolsadeideas.backend.muebleria.pdf.common.PdfHeader;
import com.bolsadeideas.backend.muebleria.pdf.common.PdfTotales;
import com.bolsadeideas.backend.muebleria.pdf.common.PdfUtils;
import com.lowagie.text.Document;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfWriter;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PresupuestoPdfService {
	
	private final PresupuestoPdfDataService presupuestoPdfDataService;
	
	private final PdfHeader pdfHeader;
	
	public byte[] generarPdf(Long ordenId) {

        // Por ahora comprobamos que los datos existen.
        var presupuesto = presupuestoPdfDataService.obtenerDatosPresupuesto(ordenId);

        //Crea un espacio en memoria donde se hara el archivo
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        //Crea conceptaulmente el archivo
        Document document = new Document(PageSize.A4, 36, 36, 36, 36);
        
        
        try {
        	//Conectamos ambos osea agregue el documento y lo agregue dentro outputStream
        	PdfWriter.getInstance(document, outputStream);
        	
        	document.open();
        	
        	/****************************************************/
            /********************* LOGO *************************/
            /****************************************************/

            Image logo = PdfUtils.cargarLogo();


            /****************************************************/
            /****************** ENCABEZADO **********************/
            /****************************************************/

            document.add(pdfHeader.crear(
                        "PRESUPUESTO",
                        String.valueOf(presupuesto.getFolio()),
                        presupuesto.getFecha(),
                        logo
                    )
            );
            
            /****************************************************/
            /********************* CLIENTE **********************/
            /****************************************************/

            document.add(PdfCliente.crear(presupuesto));
           
            /****************************************************/
            /******************* TABLA DETALLES *****************/
            /****************************************************/            
            
            document.add(PdfDetalle.crear(presupuesto.getDetalles()));
            
            /****************************************************/
            /****************** ESTADO FINANCIERO ***************/
            /****************************************************/
            
            document.add(PdfTotales.crear(presupuesto));
            
            /****************************************************/
            /***************** FIRMAS PRESUPUESTO ***************/
            /****************************************************/
            
            document.add(PdfFirmas.crear());
            
            /****************************************************/
            /*********************** FOOTER *********************/
            /****************************************************/
            
            document.add(PdfFooter.crear());

        } catch (Exception e) {

            throw new IllegalStateException("No fue posible generar el PDF del presupuesto", e);

        } finally {

            if (document.isOpen()) {
                document.close();
            }
        }


        return outputStream.toByteArray();
    }

       
	
}
