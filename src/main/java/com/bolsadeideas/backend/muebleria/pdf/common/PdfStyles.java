package com.bolsadeideas.backend.muebleria.pdf.common;

import java.awt.Color;

import com.lowagie.text.Font;


public class PdfStyles {


    private PdfStyles() {
        // Clase de utilidades: no debe instanciarse
    }


    /************************************************************/
    /************************ COLORES ***************************/
    /************************************************************/

    public static final Color COLOR_PRIMARY = new Color(28, 118, 171);

    public static final Color COLOR_PRIMARY_SOFT = new Color(232, 244, 250);

    public static final Color COLOR_TEXT = new Color(35, 35, 35);

    public static final Color COLOR_TEXT_MUTED = new Color(105, 105, 105);

    public static final Color COLOR_BORDER = new Color(215, 220, 225);

    public static final Color COLOR_BACKGROUND = new Color(248, 250, 252);

    public static final Color COLOR_WHITE = Color.WHITE;

    public static final Color COLOR_SUCCESS = new Color(40, 140, 80);

    public static final Color COLOR_DANGER = new Color(190, 55, 55);
    
    public static final Color COLOR_ROW_ALTERNATE = new Color(245, 249, 252);
    
    public static final Color COLOR_TOTAL_BACKGROUND = new Color(242, 248, 252);

    public static final Color COLOR_TOTAL_HIGHLIGHT = new Color(220, 240, 250);


    /************************************************************/
    /************************ FUENTES ***************************/
    /************************************************************/

    public static final Font FONT_EMPRESA = new Font(Font.HELVETICA, 17, Font.BOLD, COLOR_TEXT);
    
    public static final Font FONT_EMPRESA_INFO = new Font(Font.HELVETICA, 7.5f, Font.NORMAL, COLOR_TEXT_MUTED);

    public static final Font FONT_DOCUMENTO = new Font(Font.HELVETICA, 16, Font.BOLD, COLOR_PRIMARY);
    
    public static final Font FONT_FOLIO = new Font(Font.HELVETICA, 10, Font.BOLD, COLOR_TEXT);

    public static final Font FONT_SECTION_TITLE = new Font(Font.HELVETICA, 10, Font.BOLD, COLOR_PRIMARY);

    public static final Font FONT_LABEL = new Font(Font.HELVETICA, 9, Font.BOLD, COLOR_TEXT);

    public static final Font FONT_TEXT = new Font(Font.HELVETICA, 9, Font.NORMAL, COLOR_TEXT);

    public static final Font FONT_TEXT_MUTED = new Font(Font.HELVETICA, 8, Font.NORMAL, COLOR_TEXT_MUTED);

    public static final Font FONT_TABLE_HEADER = new Font(Font.HELVETICA, 8, Font.BOLD, COLOR_WHITE);

    public static final Font FONT_TABLE_TEXT = new Font(Font.HELVETICA, 8, Font.NORMAL, COLOR_TEXT);

    public static final Font FONT_TOTAL_LABEL = new Font(Font.HELVETICA, 10, Font.BOLD, COLOR_TEXT);

    public static final Font FONT_TOTAL = new Font(Font.HELVETICA, 13, Font.BOLD, COLOR_PRIMARY);
    

    public static final Font FONT_FOOTER = new Font(Font.HELVETICA, 7, Font.NORMAL, COLOR_TEXT_MUTED);


    /************************************************************/
    /************************ ESPACIADOS ************************/
    /************************************************************/

    public static final float SPACE_SMALL = 5f;

    public static final float SPACE_MEDIUM = 10f;

    public static final float SPACE_LARGE = 18f;


    /************************************************************/
    /************************ DIMENSIONES ***********************/
    /************************************************************/

    public static final float CELL_PADDING_SMALL = 5f;

    public static final float CELL_PADDING_MEDIUM = 8f;

    public static final float BORDER_WIDTH = 0.5f;

    public static final float LOGO_MAX_WIDTH = 85f;

    public static final float LOGO_MAX_HEIGHT = 70f;
}
