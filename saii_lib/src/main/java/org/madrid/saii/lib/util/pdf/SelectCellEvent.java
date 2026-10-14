package org.madrid.saii.lib.util.pdf;

import java.io.IOException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseField;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfBorderDictionary;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfFormField;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.TextField;

/**
 * Clase de apoyo para la generación de componente tipo combo para PDF
 *
 * @author everis
 * @version 1.0.0
 */
public class SelectCellEvent implements PdfPCellEvent {

    /**
     * Agrupador de elementos
     */
    protected PdfFormField grupoCombo;

    /**
     * Nombre de componente
     */
    protected String nombre;

    /**
     * Arreglo de datos a exportar
     */
    protected String[] exportados;

    /**
     * Arreglo de datos a mostrar
     */
    protected String[] opciones;

    /**
     * Tipo de letra para el componente
     */
    protected BaseFont tipoLetra;

    /**
     * Constructor de la clase
     *
     * @param grupoCombo Agrupador de elementos
     * @param nombre Nombre de componente
     * @param exportados Arreglo de datos a exportar
     * @param opciones Arreglo de datos a mostrar
     */
    public SelectCellEvent(PdfFormField grupoCombo, String nombre, String[] exportados,
            String[] opciones) {
        this.grupoCombo = grupoCombo;
        this.nombre = nombre;
        this.exportados = exportados;
        this.opciones = opciones;
        try {
            this.tipoLetra = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250,
                    BaseFont.NOT_EMBEDDED);
            this.tipoLetra.setSubset(false);
        } catch (final IOException ioe) {
            throw new ExceptionConverter(ioe);
        } catch (final DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }

    /**
     * Método para generar el componente tipo combo
     *
     * @param cell Celda donde es invocado a la clase
     * @param position Posiciones del contenedor del componente
     * @param canvases Datos del documento pdf donde se agregará el componente
     */
    @Override
    public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
        PdfWriter writer = canvases[0].getPdfWriter();

        float y = (position.getTop() + position.getBottom()) / 2;
        Rectangle rect = new Rectangle(position.getLeft() + 3, y - 10, position.getRight() - 3,
                y + 10);

        TextField tf = new TextField(writer, rect, this.nombre);
        tf.setFont(this.tipoLetra);
        tf.setFontSize(8);
        tf.setBorderStyle(PdfBorderDictionary.STYLE_BEVELED);
        tf.setVisibility(BaseField.VISIBLE);
        tf.setBorderColor(BaseColor.GRAY);
        tf.setChoiceExports(this.exportados);
        tf.setChoices(this.opciones);
        tf.setAlignment(Element.ALIGN_LEFT);
        try {
            this.grupoCombo.addKid(tf.getComboField());
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }
}
